echo "Run nutch crawler ...."

while true
do
  echo "[Inject]"
  ./runtime/local/bin/nutch inject file:///home/rombk/Project/Crawler/urls
  echo "[Generte]"
  ./runtime/local/bin/nutch generate -topN 10
  echo "[Fetch]"
  ./runtime/local/bin/nutch fetch -all
  echo "[Parse]"
  ./runtime/local/bin/nutch parse -all
  echo "[UpdateDB]"
  ./runtime/local/bin/nutch updatedb -all
  echo "[Index]"
  ./runtime/local/bin/nutch index -all
done
