<%@ include file="../component/header.jsp" %>

<head>
    <title>Connexion</title>
</head>


<div class="text-center" style="margin-left:30%;margin-right:30%;margin-top:5%;padding:10;border:2px solid gray;">

    <h1 class="h1 mb-3 font-weight-normal">Se connecter</h1>

    <form method="post" action="${pageContext.request.contextPath}<%= Routes.SIGN_IN_URI %>">
        <h2 class="h4 mb-3 font-weight-normal">
            Email :
            <input class="form-control" type="email" name="email" required>
        </h2>

        <h2 class="h4 mb-3 font-weight-normal">
            Mot de passe :
            <input class="form-control" type="password" name="password" required>
        </h2>

        <c:if test="${requestScope.errorMsg != null}">
            <p style="color:red">${requestScope.errorMsg}</p>
        </c:if>

        <input class="btn btn-lg btn-primary btn-block" type="submit" value="Connexion">
    </form>

    <hr />
    <p>Tu n'as pas encore adopté un sport ? </p>
    <a href="inscription">Créer un compte</a>

</div>


<%@ include file="../component/footer.jsp" %>