# local-repository

Simple local database file system for save some things.

## Usage

### Inicialize LocalStorageController
 
 ```
 LocalStorageController.inicialize(DATABASE_PATH);
 ```
 
 ### Create a repository
 
 ```
new LocalStorage(REPOSITORY_NAME, Class.class);
 ```
 
  ### Manage
 
  ```
LocalStorage database;
 ```
 
 - Find
 ```
database.find(TARGET_KEY);
 ```
 
  - Save
 ```
database.save(TARGET_KEY, TARGET_OBJECT);
 ```
 
   - Delete
 ```
database.delete(TARGET_KEY);
 ```
