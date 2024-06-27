# MERLOT Authorization Library

This repository contains a maven library that contains common authorization 
functionality shared amongst the microservices of the MERLOT marketplace.
It aims to abstract any authentication/authorization details from the login provider (e.g. Keycloak or SSI) away from the integrating components (the MERLOT backend services).
The services will be able to use the resulting user information like roles and user details, but does not need to handle obtaining them in the first place.

It contains functions to convert tokens from the authentication framework (Keycloak or SSI)
into Spring-usable granted authorities, 
process information about the currently selected role in the frontend and perform basic authorization checks.
Furthermore, it allows for application.yml based CORS configuration.

## Structure

```
├── src/main/java/eu/merloteducation/authorizationlibrary
│   ├── authorization    # converter, auth-check and header interceptor functionality
│   ├── config           # basic common auth configuration
```
