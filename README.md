# alexa-for-reddit

Alexa for Reddit is a custom skill designed to read the top 10
posts from the front page. 

## Intents
* Front Page
    * "Give me the front page" will return a list of the top 10 'hot' posts from the Reddit front page
* Subreddits
    * "Give me posts from [subreddit]" will return a list of the top 10 'hot' posts from the specified subreddit

## Notes

The project properties file is hidden until oAuth can be implemented (to protect my Reddit credentials). Here is an
example of what it looks like:

```
# Reddit
 
### Credentials
reddit.username=
reddit.password=
reddit.client_id=
reddit.client_secret=
 
### Service Settings
reddit.page.limit = 5
reddit.page.ceiling = 2
 
# Alexa
 
### Messages
alexa.help.repeat=Would you like the front page, or a subreddit?
alexa.misunderstand=Sorry, what was that?
alexa.welcome=Welcome! Would you like the front page, or a subreddit?
alexa.explain.intents=With Alexa for Reddit, you can either get the front page, or a subreddit. Which would you like?
alexa.connection.error=Sorry, there seems to be a problem connecting to Reddit.
```