<!DOCTYPE html>
<html>
    <head> 
        <meta charset="UTF-8">
        <title>Murach's Java Servlets and JSP</title>
        <link rel="stylesheet" href="styles/main.css" type="text/css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div class="survey-container">
            <h1>Thanks for joining our email list</h1>

            <p>Here is the information that you entered:</p>

            <label>Email:</label>
            <span>${user.email}</span><br>

            <label>First Name:</label>
            <span>${user.firstName}</span><br>

            <label>Last Name:</label>
            <span>${user.lastName}</span><br>

            <p>To enter another email address, click on the Back <br>
            button in your browser or the Return button below.</p>

            <form action="emailList" method="post">
                <input type="hidden" name="action" value="join">
                <input type="submit" value="Return">
            </form>
        </div>
    </body>
</html>