# Blog Application API Documentation

## Authentication

### 1. Login

- **URL** : `/login`
- **Method** : `POST`
- **Description** : 사용자 인증
- **Request Body** :

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

- **Response**
  - `200 OK` Successful login.

### 2. SignUp

- **URL**: `/signup`
- **Method**: `POST`
