## Deployment plan

Setup:
* OS - debian 10
* DB - postgresql 13
* OpenJDK 17

### DB changes

In postgresql create user with the same name as in application.yaml.

### OS changes

Create service for application. Steps:
1. Create file .service under /etc/systemd/system
2. Fill file with 
```[Unit]
Description=Rbapp

[Install]
WantedBy=multi-user.target
After=network.target

[Service]
Type=simple
ExecStart=sudo java -jar rbapp-0.0.1-SNAPSHOT.jar
WorkingDirectory=/home/fous363/berga-rbapp
Restart=always
RestartSec=5
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=%n
```
3. Run: systemctl start rbapp. To start the service
4. Run: systemctl enable rbapp. To start on boot