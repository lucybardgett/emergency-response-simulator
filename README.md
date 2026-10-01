# emergency-response-simulator
This is a public copy of a Java emergency response simulator developed collaboratively at the University of Exeter as part of a coursework project. 

## Quick start (local)

Requirements:
- Java 17+
- Maven 3.8+

Run the **public tests**:
```bash
mvn -q -Dtest=Public*Test test
```
## Overview
The program simulates an emergency response coordinator for a city grid requiring Ambulance, Fire and Police support. Units for each service are tracked across the grid and a combination of statues and shortest path finding is used to dispatch the best response option.

## Features
- polymorphism to model the different services
- encapsulation
- Abstraction to simplify the main CityRescue

This project gave me a chance to fully utilise the concepts of object oriented programming I had been working on throughout the term in lectures and implement the main OOP principles. I particularly Benefited from this being a Pair programming exercise giving me experience working alongside my peers and adapting to other people speed and style of programming.
