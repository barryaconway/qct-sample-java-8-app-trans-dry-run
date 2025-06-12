# File Download Server with Credential Storage

This is a Spring Boot application that provides file download functionality with credential storage capabilities.

## Features

### File Download
- Download files using unique UUIDs
- Support for HEAD and GET requests
- ETag and If-Modified-Since support for caching
- Throttled downloads to control bandwidth usage

### Credential Storage
- Securely store user credentials (username/password)
- Passwords are hashed using SHA-256 before storage
- Validate credentials
- Delete stored credentials
- Check if credentials exist for a user

## API Endpoints

### Credential Management

#### Store Credentials
```
POST /credentials/store
Content-Type: application/json

{
  "username": "user",
  "password": "password"
}
```

#### Validate Credentials
```
POST /credentials/validate
Content-Type: application/json

{
  "username": "user",
  "password": "password"
}
```

#### Check if Credentials Exist
```
GET /credentials/{username}
```

#### Delete Credentials
```
DELETE /credentials/{username}
```

### File Download

#### Download a File
```
GET /download/{uuid}/{filename}
```

#### Get File Metadata
```
HEAD /download/{uuid}/{filename}
```

## Configuration

Credential storage location can be configured using the `credential.storage.file` property in `application.properties`.