Set-Location C:\Users\tinkd\IdeaProjects\remotes\native-message-host
Stop-Process -Name "chrome"
Remove-Item result/log.txt
New-Item result/log.txt
mvn clean package
Move-Item -Path "C:\Users\tinkd\IdeaProjects\remotes\native-message-host\target\native-message-host-jar-with-dependencies.jar" -Destination "C:\Users\tinkd\Inist\native-message-host.jar" -Force
Start-Process -FilePath "C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" -ArgumentList --app=https://www6.bankline.ru/ibc/ru/login -LoadUserProfile