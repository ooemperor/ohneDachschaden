cd ../..

echo 'DROPPING TABLES'
set -o pipefail && cat ./setup_scripts/drop_inb_tables.sql | PGPASSWORD="postgres" psql -h 127.0.0.1 -U postgres -d "UDM"
echo 'CREATING TABLES'
set -o pipefail && cat ./setup_scripts/create_inb_tables.sql | PGPASSWORD="postgres" psql -h 127.0.0.1 -U postgres -d "UDM"