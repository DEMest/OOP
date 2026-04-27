rsync -av \
  --exclude='.gradle/' \
  --exclude='.idea/' \
  --exclude='build/' \
  --exclude='*.class' \
  --exclude='gradlew.bat' \
  /mnt/c/Users/Даниил/Desktop/main/ДЗ/Программирование/OOP/Task_2_3_1/ rootuser@100.72.120.53:~/snake/