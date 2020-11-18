This project runs on http://localhost:8888/.
Start project using Maven (mvn jetty:run).
SDK used  11.0.8.
SQL script for creating database tables can be found in shop -> src -> main -> sql.
Database config variables can be found in shop -> src -> main -> resources -> db_config.properties
 



## Tasks Sprint 1

1. As a Developer, I want to have a version-controlled Java project, where a Java webserver serves requests. So that I can start developing in a sandboxed environment.
    - Given I startup my Java web-application server, when I open `http://localhost:8888` in my browser, then ensure the server gives back an index page

2. As a User, I want to have an index page, where I can see the list of Products within a default Product Category, so that I can browse Products within that Category.
    - Given I have Products and a default Product Category in the application when I open the root url (`/`) then ensure I can see a list of Products with the following details: product title, description, image, price

3. As a User, I want to have an index page, where I can filter Products by Product Categories so that I can browse Products within any Category.
    - Given I have Products and Product Categories listed on the index page when I click on a Category's title then ensure it displays the Products only in the selected Category

4. As a User, I want to have an index page, where I can filter Products by Suppliers so that I can browse Products by Suppliers too.
    - Given I have Products and Suppliers listed on the index page when I click on a Supplier's name then ensure it displays the Products only for the selected Supplier

5. As a User, I want to have a Shopping Cart so that I can add products which I want to buy.
    - Given I have a Product list and the Products have an "Add to cart" button when I click on the "Add to cart" button then ensure it creates a new Order for storing cart data of the User and ensure it creates a new LineItem with the quanity (default: 1) and price (the price of the Product) and ensure it stores this data on the server
    - Given I have a Product list and the Products have an "Add to cart" button when I click on the "Add to cart" button then ensure the number of cart items is displayed in the Page header

6. As a User, I want to review my Shopping Cart so that I can review the items in my shopping cart before checking out so that I can see what I've already selected.
    - Given I have a Shopping Cart with items in it when I click on the "Shopping cart" menu item in the Page header then ensure it displays the items (LineItems) with the following data: name of the Product, quantity, unit price / subtotal price
    - Given I have a Shopping Cart with items in it when I click on the "Shopping cart" menu item in the Page header ensure the total price of all the items in the cart is displayed

7. As a User, I want to edit the items in my Shopping Cart so that I can modify the items when I change my mind - by changing quantity or removing items.
    - Given I have a Shopping Cart review page and the LineItems are displayed in a form and the quantities are displayed in input fields when I change the quantity of an item then ensure it stores the new quantity of the LineItem
    - Given I have a Shopping Cart review page and the LineItems are displayed in a form and the quantities are displayed in input fields when I change the quantity to 0 then ensure it removes the LineItem from the cart

8. As a User, I want to checkout the items from the Shopping Cart so that I can order the Products.
    - Given I have a Shopping Cart review page when I click on the "Checkout" button then ensure it asks the following data from the User: Name, Email, Phone number, Billing Address (Country, City, Zipcode, Address), Shipping Address (Country, City, Zipcode, Address)
    - Given I have a Shopping Cart review page when I click on the "Go to Payment" button then ensure Ensure that data on the checkout form are validated
    - Given I have a Shopping Cart review page when I click on the "Go to Payment" button and data are validated successfully then ensure that data on the checkout form are stored in the Order
    - Given I have a Shopping Cart review page when I click on the "Go to Payment" button and data are validated successfully then ensure that it redirects to the Payment page

9. As a User, I want to pay for my Products so that I can complete the payment online.
    - Given I checked out the items from the Shopping cart then I can see the total price what I have to pay
    - Given I checked out the items from the Shopping cart then I can choose from the following payment methods: credit card, paypal
    - Given I checked out the items from the Shopping cart and chosen the payment method then based on the selected payment method I can enter the credentials for the payment provider: card number, card holder, expiry date, code (in case of credit card), username and password (in case of paypal)

10. As a User, I want to see the result of my payment so that I can get a confirmation about my Order.
    - Given I made a payment and there was an error in the transaction then I can see the details of the error
    - Given I made a payment and the transaction was successful then I can see the Confirmation page with the details of the Order
    - Given I made a payment and the transaction was successful then then ensure it saves the Order into a `JSON` file
    - Given I made a payment and the transaction was successful then ensure it sends an email to the User about the Order

11. As an Admin, I want to have a logfile about the checkout processes (per Order) so that I can see the steps of every Order and investigate issues.
    - Given the User started a checkout process then ensure it saves all the steps and details into a JSON file (where the filename is the Order ID and Date)

# Codecool shop (sprint 2)

## Story

It's time to enhance the Online Shop, an online eCommerce web-application with Java.
Where users can not only browse products, add them into a Shopping Cart,
checkout items and make payments. But also can log in and see the abandoned shopping cart or order history.

> Did you know that the very first product on eBay was a broken laser pointer?
> If you don't believe, check their history: [eBay history](https://www.ebayinc.com/company/our-history/)


## Tasks Sprint 2

1. Create a new sprint tab on the existing backlog. Last week you had a long list of stories, a few new stories this week.
    - The new items are added to the backlog
    - The team has created a sprint 2 plan based upon the unified backlog

2. As you will work in a new repository but you need the code from the previous sprint, add the `codecool-shop-2` repository as a new remote to the previous sprint's repository, then pull (merge) and push your changes into it.
    - There is a merge commit in the project's repository that contains code from the previous sprint

3. As an Operator of the Shop, I want the product data to be persistent. So that I can restart the application without loosing product data.
    - There is an empty PostgreSQL database called `codecoolshop` (url: localhost:5432, usr/pwd: postgres/postgres)
    - There is a initializer script file `src/main/sql/init_db.sql`. When I run the script file then all of the empty tables are created that will store `Products`, `ProductCategories` and `Suppliers`.
    - When I add `Product` or `ProductCategory` or `Supplier`  via `ProductDaoJdbc` or `ProductCategoryDaoJdbc` or `SupplierDaoJdbc` then I want them appear in a DB table in a PostgreSql database called `codecoolshop`.
    - Given that I have some `Products` with `ProductCategories` and `Suppliers` in the webshop. When I restart the application then the Products with ProductCategories and Suppliers should remain.

4. As a Developer, I want to cover the `ProductDao`, `ProductCategoryDao`  and `SupplierDao` memory implementation with tests. So that I can safely change the implementation later.
    - All methods of the DAOs should be tested where test cases test DAOs through interfaces,  so that we can change the implementation in the whole test class just by changing a single line.
    - When I run the tests from IDE then I see at least 11 passed tests.

5. As a Developer, I want to cover the `ProductDao`, `ProductCategoryDao`  and `SupplierDao` JDBC implementation with tests. So that I can ensure them working correctly.
    - All methods of all JDBC DAO should be tested.
    - Given I have a PostgreSQL database called `codecoolshop` (url: localhost:5432, usr/pwd: postgres/postgres) with empty tables for `Product`, `ProductCategories` and `Suppliers` When I run the tests from IDE then I see at least 11 passed tests.  

6. As a Developer, I want to read the DB connection parameters (url, database name, usr, pwd)  from a config files so that I can change the database under the application.
    - Given that I have the config file `src/main/resources/connection.properties`
with the following structure:
```
url: localhost:5432
database: codecoolshop
user: postgres
password: postgres```

7. As a Developer, I want to use separate database for tests, so that test does not influence the production database.
    - There is an initialized PostgreSQL database called `codecoolshop_test` (url: localhost:5432, usr/pwd: postgres/postgres) with empty tables: `Product`, `ProductCategories` and `Suppliers`
    - There is a separated config file for tests.
    - When I run the DB DAO tests than all test passes and the production database remains untouched.

8. As an Operator of the Shop,  I want to keep Order data safe and persistent, so that I won't loose money because of technical issues.
    - Given the User started a checkout process. Then ensure it saves all Order data into database (in each and every step, except cart).

9. As a User, I want to sign up (make a personal account) so that I can store Orders to my personal account.
    - there is a `Sign up` option on the website
    - it has a form with all the required fields:
- `name`
- `email`
- `password`
    - when the user submits the form with correct/valid information then ensure the system saves it's data as a new `User`
    - the system sends a welcome email after successful registration
    - When the User submits the form with incorrect/invalid information then ensure the program shows the same form with the incorrect data, and some description about the errors.

10. As a User, I want to able to login, so I can authenticate myself and access my personal data. I want to be able to logout so I can close my session.
    - There is a `Login` menu on the website
    - When the user chooses the "Login" menu
then ensure to provide a login form with the following fields:
- email address
- password
    - When the user submits the form with valid information then authenticate and give a new logged-in session to the User
    - When the user submits the form with invalid information then provide an error message
    - Ensure to provide a Logout option for loggend in users. When the user chooses the "Logout" option then reset the session and redirect back to the login form.

11. As a loggedin User, I want to see my Order history, so that I can see my previous Orders and follow their status.
    - There is an `Order history` menu item
    - provide a list with all the Orders of that user, with the following details:
- order date
- order status (checked / paid / confirmed / shipped)
- total price
- product list (with product name, price)

12. As a logged-in User, I want to save the current items of my Shopping cart so that I can order my selected Products later.
    - there is a `Save my cart` button (on the Shopping cart review page)
    - by clicking on this button the system saves the cart items into the database - for that loggedin User
    - Given that there is a User with a previously saved shopping cart. When the user finished the login process then ensure to refill the user's shopping cart with the saved items.

13. As a loggedin User, I want to save my billing and shipping info (to my personal account) so that I don't need to type these data all the time - during checkout.
    - there is a `Billing info` menu item
    - after clicking on the menu a provide a form where the user can fill in
the personal billing and shipping info (what is needed for the checkout)
    - Given there's a Shopping Cart review page. When I click on the "Checkout" button then ensure the system shows the pre-filled billing and shipping info on the checkout form.

