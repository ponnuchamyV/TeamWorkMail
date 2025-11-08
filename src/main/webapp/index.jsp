<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>Register</title>
</head>
<body>
<h2>Register</h2>
<form method="post" action="${pageContext.request.contextPath}/register">
  <label>First name: <input type="text" name="firstname" required /></label><br/><br/>
  <label>Last name:  <input type="text" name="lastname" required /></label><br/><br/>
  <label>Email:      <input type="email" name="email" required /></label><br/><br/>
  <label>Phone:      <input type="text" name="phone" pattern="[0-9+\\- ]{6,20}" required /></label><br/><br/>
  <button type="submit">Submit</button>
</form>
</body>
</html>
