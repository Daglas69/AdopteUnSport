<h4> Mes informations </h4>

<p>Username : ${requestScope.jwtUser.username}</p>
<p>Email : ${requestScope.jwtUser.email}</p>

<a href="?page=Informations&action=ModifMdp">
    <button class="btn btn-primary">Modifier mot de passe</button>
</a>

<c:if test="${requestScope.successMsg != null}">
    <p style="color:green">${requestScope.successMsg}</p>
</c:if>
<c:if test="${requestScope.errorMsg != null}">
    <p style="color:red">${requestScope.errorMsg}</p>
</c:if>

<c:if test="${param.action == 'ModifMdp'}">
    <div style="margin-left:40%;margin-right:40%;margin-top:20">
        <form method="post" action="${pageContext.request.contextPath}<%= Routes.ACCOUNT_DETAILS_URI %>">
            <input type="hidden" name="page" value="Informations">
            <input type="hidden" name="action" value="ModifMdp">

            <p class="mb-3 font-weight-normal">
                Mot de passe actuel :
                <input class="form-control" type="password" name="currentPassword" required>
            </p>
            <p class="mb-3 font-weight-normal">
                Nouveau mot de passe :
                <input class="form-control" type="password" name="newPassword" required>
            </p>
            <p class="p mb-3 font-weight-normal">
                Confirmez nouveau mot de passe :
                <input class="form-control" type="password" name="confirmPassword" required>
            </p>

            <button class="btn btn-lg btn-danger" type="submit" name="submitBtn" value="changePassword">
                Valider
            </button>
        </form>
    </div>
</c:if>