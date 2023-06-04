# Atipera GitHub API

Welcome to the Atipera GitHub API. This API provides a convenient and efficient way to retrieve essential information about GitHub repositories, including repository names, ownership, and details about each branch's last commit.

## Endpoints

### Get User Repositories

Use the following endpoint to retrieve repositories associated with a specific user:

`
/api/github/users/{username}/repositories
`

Replace `{username}` with the actual GitHub username for which you want to retrieve the repositories.

## Error Responses

The API supports the following error responses:

### - User Not Found

Status code: 404

```
{
  "status": 404,
  "message": "User '{username}' does not exist"
}
```

This response indicates that the requested user does not exist on GitHub.
### - Unsupported Format

Status code: 406

```
{
  "status":406,
  "message":"Requested content type is not supported"
}
```

This response is returned when a user requests a format other than JSON.

## Rate Limiting

By default, the GitHub API allows a maximum of 60 requests per hour for unauthenticated users. However, once you log in and provide your GitHub username and access token, the rate limit increases to 5,000 requests per hour.

To set your GitHub username and access token, please update the `src/main/resources/application.properties` file with the following information:

```java
github.username={username}
github.token={token}
```

Replace `{username}` with your actual GitHub username and `{token}` with your access token.

For more information on managing your personal access tokens, refer to the [GitHub documentation](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens).

## 

Thank you for using the Atipera GitHub API. If you have any questions or need further assistance, please don't hesitate to contact us.
