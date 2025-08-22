cd ../..

echo '####  LOADING STAGE  ####'
echo 'inb_stage_admin' && set -o pipefail && cat ./import/sql/load_inb_stage_admin.sql | PGPASSWORD="postgres" psql -h 127.0.0.1 -U postgres -d "UDM"
echo '####  LOADING  ####'
echo 'inb_admin' && set -o pipefail && cat ./import/sql/load_inb_admin.sql | PGPASSWORD="postgres" psql -h 127.0.0.1 -U postgres -d "UDM"
echo 'Loading done'