<?php
  session_start();
  require "db_connect.php";

  $username = $_POST['username'];
  $password = $_POST['password'];

  $password = md5($password);

  $login_query = "SELECT Username FROM User WHERE Username = '$username' AND Password= '$password'";

  $query_result = mysqli_query($connection,$login_query) or die (mysqli_error($connection));

  if (mysqli_affected_rows($connection) == 1) {
    echo "LOGIN OK";

    // $row = mysqli_fetch_row($query_result);

    $_SESSION['username'] = $username;
    $_SESSION['login'] = "OK";
  }
  else {
    echo "LOGIN ERROR";
  }
?>
