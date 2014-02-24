<?php

//load and connect to MySQL database stuff
require("config.inc.php");
$login_ok= false;
if (!empty($_POST)) {
    //gets user's info based off of a username.
    $query = " 
            SELECT 
                id, 
                username, 
                password
            FROM users WHERE 
                username = :username 
        ";
    
    $query_params = array(
        ':username' => $_POST['username']
    );
    
    try {
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        // For testing, you could use a die and message. 
        //die("Failed to run query: " . $ex->getMessage());
        
        //or just use this use this one to product JSON data:
        $response["success"] = 0;
        $response["message"] = "Database Error1. Please Try Again!";
        die(json_encode($response));
        
    }
    
    //This will be the variable to determine whether or not the user's information is correct.
    //we initialize it as false.
    $validated_info = false;
    
    //fetching all the rows from the query
    $row = $stmt->fetch();
    if ($row) {
        //if we encrypted the password, we would unencrypt it here, but in our case we just
        //compare the two passwords
        if ($_POST['password'] === $row['password']) {
            $login_ok = true;
        }
    }
    
    // If the user logged in successfully, then we send them to the private members-only page 
    // Otherwise, we display a login failed message and show the login form again 
    if ($login_ok) {
        $response["success"] = 1;
        $response["message"] = "Login successful!";
        die(json_encode($response));
    } else {
        $response["success"] = 0;
        $response["message"] = "Invalid Credentials!";
        die(json_encode($response));
    }
} else {
?>
		<h1>Login</h1> 
		<form action="login.php" method="post"> 
		    Username:<br /> 
		    <input type="text" name="username" placeholder="username" /> 
		    <br /><br /> 
		    Password:<br /> 
		    <input type="password" name="password" placeholder="password" value="" /> 
		    <br /><br /> 
		    <input type="submit" value="Login" /> 
		</form> 
		<a href="register.php">Register</a>
		
		
		<h3>Kullanýmý</h3>
		<p>.../login.php dosyasýna istek olarak, <b>Post</b> metodu ile "<b>username</b>" ve "<b>password</b>" gönderilir.</p>
		<p>Cevap olarak  <b>{"success":1,"message":"Login successful!"}</b> json veriyapýsý geri döndürülür.</p>
		<p><b>"success":1</b> tam sayý(int) deðeri olarak 1 ise oturum baþarýlý, 0 ise baþarýsýz. </p>
		<p><b>"message":"Login successful!"</b> string olarak gerçekleþen olayla ilgili bilgi verir. 
		<p>Örneðin; <b>"Login successful!"</b>, <b>"Invalid Credentials!"</b> test aþamasýnda iken <b>"Database Error1. Please Try Again!"</b> gibi.</p>
		</p>
		
	<?php
}

?>