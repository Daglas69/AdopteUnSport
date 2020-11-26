<%@ include file="../component/header.jsp" %>

<head>
    <title>Créer un compte</title>
</head>

<script src="https://www.google.com/recaptcha/api.js" async defer></script>

<div class="text-center" style="margin-left:30%;margin-right:30%;margin-top:5%;padding:10;border:2px solid gray;">
    <h1 class="h1 mb-3 font-weight-normal" >Créer un compte</h1>

    <form class="form-signin" id="signup-form" method="post" 
        action="${pageContext.request.contextPath}<%= Routes.SIGN_UP_URI %>">
        <h4 class="mb-3 font-weight-normal">
            Username* : <i class="far fa-question-circle" aria-hidden="true" title="Votre nom d'utilisateur peut contenir seulement des caractères alphanumériques."></i>
            <input class="form-control" type="text" name="username" minlength="5" maxlength="30" required
            placeholder="Nom d'utilisateur">
        </h4>

        <h4 class="mb-3 font-weight-normal">
            Email* :
            <input class="form-control" type="email" name="email" required
            placeholder="Email">
        </h4p>

        <h4 class="mb-3 font-weight-normal">
            Mot de passe* : <i class="far fa-question-circle" aria-hidden="true" title="Le mot de passe doit contenir entre 5 et 30 caractères, dont un chiffre, une lettre minuscule et majuscule, et un caractère spécial : @ # $ % ^ . & + ="></i>
            <input class="form-control" type="password" name="password" required minlength="5" maxlength="30"
            placeholder="Mot de passe">
        </h4>
        

        <h4 class="mb-3 font-weight-normal">
            Confirmation du mot de passe* :
            <input class="form-control" type="password" name="confirmPassword" required minlength="5" maxlength="30"
            placeholder="Confirmez votre mot de passe">
        </h4>

        <c:if test="${requestScope.errorMsg != null}">
            <p style="color:red">${requestScope.errorMsg}</p>
        </c:if>

        <div class="g-recaptcha" data-sitekey="6Le2WOoZAAAAANyO5jktUqL8DRU9IJAPyJa9YKd-"></div>
        <br />

        <input class="btn btn-lg btn-primary btn-block" type="submit" name="submitBtn" value="Inscription">
    </form>

    <hr />
    <p>Tu as déjà adopté un sport ? </p>
    <a href="connexion">Se connecter</a>

</div>


<%@ include file="../component/footer.jsp" %>