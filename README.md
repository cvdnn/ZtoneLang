## Command line instructions

### Git global setup

```
git config --global user.name "handy444"
git config --global user.email "handyworkspace@gmail.com"
```

### Create a new repository

```
git clone https://code.aliyun.com/ztone/ZtoneLang.git
cd ZtoneLang
touch README.md
git add README.md
git commit -m "add README"
git push -u origin master
```

### Existing folder or Git repository

```
cd existing_folder
git init
git remote add origin https://code.aliyun.com/ztone/ZtoneLang.git
git add .
git commit
git push -u origin master
```

### Maven

```
./gradlew -q -p ztone.lang clean build uploadArchives && ./gradlew -q -p frame.network clean build uploadArchives && ./gradlew -q -p frame.bus clean build uploadArchives && ./gradlew -q -p frame.analytics clean build uploadArchives && ./gradlew -q -p frame.analytics.man clean build uploadArchives
```