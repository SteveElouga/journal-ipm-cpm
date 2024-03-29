
# IPM-journal Back-end

Bienvenue dans ce projet Spring Boot! Ce README vous fournit les informations éssentielles pour comprendre et exécuter ce projet.

## Description

Ce projet est une application Spring Boot qui présente l'inscription et la connexion d'un utilisateur faisant ici réference à l'authentification et aux differentes autorisations configurées.

## Fonctionnatités

- Création, lecture, mise à jour et suppression (CRUD) des utilisateurs.
- Utilisation de JPA pour la couche de persistance.
- Documentation de l'API avec Swagger, disponible [ici](http://localhost:8080/cpmipmjournal/swagger-ui/index.html#/) (A fournir!).

## Prérequis

Avant d'exécuter ce projet, assurez-vous d'avoir installé :
- Java JDK (version 11 minimun!), disponible [ici](https://www.oracle.com/java/technologies/downloads/).
- Apache Maven, disponible [ici](https://maven.apache.org/download.cgi).
- Un IDE de votre choix (recommandé: [IntelliJ IDEA](https://www.bing.com/ck/a?!&&p=0678ba8d7403359dJmltdHM9MTcwNzY5NjAwMCZpZ3VpZD0wNTYxZTYwMS1hODQzLTY3ZDItMWNhNi1mNTYzYTlhYjY2NjImaW5zaWQ9NTQ4Mw&ptn=3&ver=2&hsh=3&fclid=0561e601-a843-67d2-1ca6-f563a9ab6662&psq=intellij&u=a1aHR0cHM6Ly93d3cuamV0YnJhaW5zLmNvbS9pZGVhL2Rvd25sb2Fk&ntb=1), [Eclipse](https://www.bing.com/ck/a?!&&p=fa1c8462dfa7f84aJmltdHM9MTcwNzY5NjAwMCZpZ3VpZD0wNTYxZTYwMS1hODQzLTY3ZDItMWNhNi1mNTYzYTlhYjY2NjImaW5zaWQ9NTQ3MQ&ptn=3&ver=2&hsh=3&fclid=0561e601-a843-67d2-1ca6-f563a9ab6662&psq=eclipse+ide&u=a1aHR0cHM6Ly93d3cuZWNsaXBzZS5vcmcvZG93bmxvYWRz&ntb=1)).

## Installation

1. Cloner le projet depuis le dépôt Git :

   git clone https://github.com/JamesAlaric/ipm-journal.git


2. Importer le projet dans votre IDE et laisser Maven importer les dépendances.

3. Configurer votre base de données dans src/main/resources/application.properties.

4. Exécuter l'application en utilisant la classe ProjetSpringBootApplication.


## API

Method | Path           | Description                    |
-------|----------------|--------------------------------|
GET    | /api/users/allUsers     | Récupérer tous les utilisateurs     |
GET    | /api/users/{id} | Récupérer un utilisateur par ID |
GET    | /avis | Récupérer tous les avis utilisateur |
POST   | /api/users/inscription      | Enregistrer un utilisateur/ inscription d'un utilisateur            |
POST   | /api/users/inscriptionAdmin      | Enregistrer un utilisateur(Admin) / inscription d'un utilisateur(Admin)            |
POST   | /api/users/activation      | Activer le compte d'un utilisateur inscript            |
POST   | /api/users/connexion      | connexion d'un utilisateur            |
POST   | /api/users/deconnexion      | deconnexion d'un utilisateur            |
POST   | /api/users/modifier-mot-de-passe      | Demande de modification du mot de passe d'un utilisateur            |
POST   | /api/users//nouveau-mot-de-passe      | Modification du mot de passe d'un utilisateur            |
POST   | /avis      | Enregistrer un avis utilisateur            |
PUT    | /api/users/{id}      | Modifier un utilisateur par ID    |
DELETE | /api/users/{id} | suppression d'utilisateur par ID   |

### Utilisation
```
// GET /api/users/allUsers
$ curl -X GET http://localhost:8080/cpmipmjournal/api/users/allUsers -i 
    -H "Content-Type: application/json"
    -H "Authorization: Bearer VOTRE_TOKEN"

// GET /api/users/{id}
$ curl -X GET http://localhost:8080/cpmipmjournal/api/users/1 -i 
    -H "Content-Type: application/json"
    -H "Authorization: Bearer VOTRE_TOKEN"

// GET /avis
$ curl -X GET http://localhost:8080/cpmipmjournal/avis -i 
    -H "Content-Type: application/json"
    -H "Authorization: Bearer VOTRE_TOKEN"

// POST /api/users/inscription
$ curl -X POST http://{host}:{port}/cpmipmjournal/api/users/inscription -i
    -H "Accept: application/json"
    -H "Content-Type: application/json"
    -d 
        '{
            "firstname":"Elouga",
            "lastname":"Steve",
            "email":"nyobe5@gmail.com",
            "nationality":"Camerounais",
            "password":"12345"
        }'

// POST /api/users/inscriptionAdmin
$ curl -X POST http://{host}:{port}/cpmipmjournal/api/users/inscriptionAdmin -i
    -H "Accept: application/json"
    -H "Content-Type: application/json"
    -d 
        '{
            "firstname":"Elouga",
            "lastname":"Steve",
            "email":"nyobe5@gmail.com",
            "nationality":"Camerounais",
            "password":"12345"
        }'    

// POST /api/users/activation
$ curl -X POST http://{host}:{port}/cpmipmjournal/api/users/activation -i
    -H "Accept: application/json"
    -H "Content-Type: application/json"
    -H "Authorization: Bearer VOTRE_TOKEN"
    -d 
        '{
            "code":"393615"
        }'

// POST /api/users/connexion
$ curl -X POST http://{host}:{port}/cpmipmjournal/api/users/connexion -i
    -H "Accept: application/json"
    -H "Content-Type: application/json"
    -d 
        '{
            "username":"nyobeelouga@gmail.com",
            "password":"1234567890"
        }'

// POST /api/users/deconnexion
$ curl -X POST http://{host}:{port}/cpmipmjournal/api/users/deconnexion -i    
    -H "Authorization: Bearer VOTRE_TOKEN"

// POST /api/users/modifier-mot-de-passe
$ curl -X POST http://{host}:{port}/cpmipmjournal/api/users/modifier-mot-de-passe -i
    -H "Accept: application/json"
    -H "Content-Type: application/json"
    -d 
        '{
            "username":"nyobeelouga@gmail.com"
        }' 

// POST /api/users/nouveau-mot-de-passe
$ curl -X POST http://{host}:{port}/cpmipmjournal/api/users/nouveau-mot-de-passe -i
    -H "Accept: application/json"
    -H "Content-Type: application/json"
    -d 
        '{
            "username":"nyobeelouga@gmail.com",
            "code":"245609",
            "password":"1234567890"
        }'

// POST /avis
$ curl -X POST http://{host}:{port}/cpmipmjournal/avis -i
    -H "Accept: application/json"
    -H "Content-Type: application/json"
    -H "Authorization: Bearer VOTRE_TOKEN"
    -d 
        '{
            "message":"Bon tutoriel!"
        }'        

// PUT /api/users/{id}
$ curl -X PUT http://{host}:{port}/cpmipmjournal/api/users/1 -i 
    -H "Accept: application/json" 
    -H "Authorization: Bearer VOTRE_TOKEN"
    -H "Content-Type: application/json"
    -d 
        '{
            "firstname":"Steve",
            "lastname":"Nyobe",
            "email":"steve@gmail.com",
            "nationality":"Senegalais"
        }'

// DELETE /api/users/{id}
$ curl -X DELETE http://{host}:{port}/cpmipmjournal/api/users/1 -i
    -H "Authorization: Bearer VOTRE_TOKEN"



Assurez-vous d'utiliser un outil comme Postman pour tester les endpoints.
```

## IHM (Interface Homme-Machine)

Il est possible de voir une utilisation de l'API au travers une IHM web accessible à l'adresse : http://localhost:8080/journal.com (Non disponible).


## Auteur

Ce projet a été développé par [Elouga Nyobe](https://github.com/SteveElouga).
