cd ../..

echo '####  LOADING  ####'
echo 'inb_gvb' && set -o pipefail && cat ./import/sql/load_inb_gvb.sql | PGPASSWORD="postgres" psql -h 127.0.0.1 -U postgres -d "UDM"
echo 'Loading done'