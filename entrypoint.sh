#!/bin/sh
set -e

# Support DATABASE_URL or SPRING_DATASOURCE_URL in any format
RAW_URL="${DATABASE_URL:-$SPRING_DATASOURCE_URL}"

if [ -n "$RAW_URL" ]; then
    case "$RAW_URL" in
        *@*)
            # Strip protocol prefix (postgres://, postgresql://, or jdbc:postgresql://)
            NOPROTO=$(echo "$RAW_URL" | sed -e 's|^[a-zA-Z0-9:]*//||')
            
            # Extract user:pass (before @)
            USERPASS=$(echo "$NOPROTO" | cut -d'@' -f1)
            USER=$(echo "$USERPASS" | cut -d':' -f1)
            PASS=$(echo "$USERPASS" | cut -d':' -f2-)
            
            # Extract host/db (after @)
            HOSTDB=$(echo "$NOPROTO" | cut -d'@' -f2-)
            
            export SPRING_DATASOURCE_URL="jdbc:postgresql://${HOSTDB}"
            export SPRING_DATASOURCE_USERNAME="$USER"
            export SPRING_DATASOURCE_PASSWORD="$PASS"
            ;;
        *)
            case "$RAW_URL" in
                jdbc:postgresql://*)
                    export SPRING_DATASOURCE_URL="$RAW_URL"
                    ;;
                postgres://*)
                    export SPRING_DATASOURCE_URL=$(echo "$RAW_URL" | sed 's|^postgres://|jdbc:postgresql://|')
                    ;;
                postgresql://*)
                    export SPRING_DATASOURCE_URL=$(echo "$RAW_URL" | sed 's|^postgresql://|jdbc:postgresql://|')
                    ;;
            esac
            ;;
    esac
fi

echo "Connecting to PostgreSQL at: ${SPRING_DATASOURCE_URL} as user: ${SPRING_DATASOURCE_USERNAME}"
echo "Starting PeopleOps HRMS on port: ${PORT:-8080}..."
exec java -Dserver.port="${PORT:-8080}" -jar app.war
