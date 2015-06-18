<?php
// check if fields passed are empty
if(empty($_POST['name'])  		||
   empty($_POST['email']) 		||
   empty($_POST['message'])	||
   !filter_var($_POST['email'],FILTER_VALIDATE_EMAIL))
   {
	echo "No arguments Provided!";
	return false;
}
	
$name = $_POST['name'];
$email_address = $_POST['email'];
$message = $_POST['message'];
	
// Create the email and send the message
$to = 'nicolas.hinderling@gmail.com'; // put your email
$email_subject = "New Email From:  $name";
$email_body = "Name: $name \n".
				  "Email: $email_address\n\nMessage: \n$message";
$headers = "From: noreply@nicohinderling.me\n";
$headers .= "Reply-To: $email_address";	
mail($to,$email_subject,$email_body,$headers);
return true;			
?>
