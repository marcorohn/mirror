# Mirror

**tldr:** Translate DTOs from Java to Typescript (in theory any language) automatically

One reason why choosing to use javascript/typescript in the backend of some web application for sure is the possibility to recycle classes, types, interfaces, etc => typesafety.
You'd normally want to be sure the structure of the data sent by the server does not differ from what the client expects and vice versa. So, when using the same language in client and server, just import the files in both projects, and you're done.
In any other cases, this is not possible (obviously), so you'd have to manually code DTOs for both platforms.
This project aims to convert Java-DTO-classes to typescript interfaces, to solve the described problem. A common usecase would be i.e. a spring-boot-server and an angular client, however, mirror is extensible, so you could add some custom logic to generate i.e. C structs as output (whyever you would do this).
Furthermore, the source does not have to be a server, you could for example have a nest server and a java client, however you would need to specify the types in the client in that case.

examples and usage follows
