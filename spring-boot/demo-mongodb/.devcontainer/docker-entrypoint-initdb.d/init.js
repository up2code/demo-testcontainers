db = new Mongo().getDB("library");

db.createUser(
    {
        user: "test-user",
        pwd: "password",
        roles:[
            {
                role: "readWrite",
                db:"library"
            }
        ]
    })


db.createCollection("books");
db.books.insertMany([
    {
        "id": 1,
        "title": "The Lord of the Rings",
        "author": "J.R.R. Tolkien",
        "isbn": "9780547928227",
        "created": ISODate("2024-02-08T19:26:00Z")
    },
    {
        "id": 2,
        "title": "The Hitchhiker's Guide to the Galaxy",
        "author": "Douglas Adams",
        "isbn": "9780345391803",
        "created": ISODate("2024-02-08T19:26:00Z")
    },
    {
        "id": 3,
        "title": "Pride and Prejudice",
        "author": "Jane Austen",
        "isbn": "9780141439518",
        "created": ISODate("2024-02-08T19:26:00Z")
    }
]);