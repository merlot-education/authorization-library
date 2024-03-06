# MERLOT Authorization Library

This repository contains a maven library that contains common authorization 
functionality shared amongst the microservices of the MERLOT marketplace.

It contains functions to convert tokens from the authentication framework (Keycloak)
into Spring-usable granted authorities, 
process information about the currently selected role in the frontend and perform basic authorization checks.
Furthermore, it allows for application.yml based CORS configuration.

## Structure

```
├── src/main/java/eu/merloteducation/authorizationlibrary
│   ├── authorization    # converter, auth-check and header interceptor functionality
│   ├── config           # basic common auth configuration
```
