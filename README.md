# Domain model

There are two domain objects in the model: the Subject and the User Group.

Subject represents a user. It has an id, a username, a password and a holds references to the groups that the user is member of.

User Group is used to organize the users in sets. It has an id and a name. 

# Security

Each request can contain a valid JWT token as an Authorization HTTP header. If it is present, 
the application verifies this token and authorizes the user.

In case of an incorrect token the request is blocked.

If no token is provided, the user proceeds as anonymous.

# Use cases

### User registration

A new user can be created via REST. This endpoint is available to everyone.

### User login

This endpoint is also not protected. After submitting a user's username and password, the app verifies the credentials and
in case of success, returns a JWT token in the following format:

```json
{
  "sub": "<user_id>",
  "username": "<user_username>",
  "groups": [
    {
      "id": "<group_id>",
      "name": "<group_name>"
    }
  ]
}
```

### Group management

It is possible to perform CRUD on groups as an authenticated user.

# Task description

Implement the use cases and requirements mentioned above. The statements are vague on purpose, let your creativity loose!
We expect the described minimal requirements to be met, but we also welcome any industry best practises / optional show-off
of your skills and ideas. The latter don't have to be complete across all endpoints and entities, it is enough to demonstrate them.

We have provided some scaffolding, classes and dependencies that we thought might be useful. 
Feel free to switch them up if you have other ideas. The requirements for this are the following:

* Docker
* JRE

Running the application:

```docker-compose up```

```./gradlew bRu```
