# Mazzady-xml-task
# Overview

Test application to retrieve data and with dynamic and static view 

# Technologies

The project uses modern Android tools and frameworks such as Kotlin, xml, Coroutines, Live Data,
Hilt , clean code, compose navigation and others. The app employs automatic state and error handling
and wrappers to handle backend API calls.

## Testing

Illustration integration with Espresso Testing

# Project Structure

The project uses a MVVM architecture. The app is primarily backend driven and can only function when
the user has an internet connection. There is no local database, as the needed data is retrieved
fresh from the backend. Business logic and use cases are placed in a separate Repository layer.
View Models are used to communicate with the UI layer, which is primarily written with the Compose
framework.

# File Structure

The project uses a Clean architecture. All UI related code can be found in the ".presentation"
package and is subdivided into packages according to view
".domain" package contains systems models , repositories, mappers..
".data" pancake contains implementation of data source "Retorfit" and date transfer objects

# Visuals
![1](https://user-images.githubusercontent.com/74387512/229310753-9fbdf2ee-0055-4a7b-bea5-f64c0b25d347.png)
![2](https://user-images.githubusercontent.com/74387512/229310758-e2f7d514-13b1-4ff7-bc1e-419b0ba443f6.png)
![3](https://user-images.githubusercontent.com/74387512/229310763-548bdaa3-8265-4dcf-b24b-da721d361c9a.png)
![4](https://user-images.githubusercontent.com/74387512/229310769-56db93b9-e8f9-4f7e-a718-44a0f98093f9.png)
