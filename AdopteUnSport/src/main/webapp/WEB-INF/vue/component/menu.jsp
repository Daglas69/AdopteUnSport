<nav class="navbar navbar-expand-lg navbar-dark bg-dark static-top">
  <div class="container">
    <a class="navbar-brand" href="${pageContext.request.contextPath}<%= Routes.HOME_URI %>">
        <img src="${pageContext.request.contextPath}/static/img/logo.png" alt="logo" width="200" height="80">
    </a>

    <div class="collapse navbar-collapse" id="navbarResponsive">
        <ul class="navbar-nav ml-auto">

            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}<%= Routes.HOME_URI %>">
                    Accueil
                </a>
            </li>

            <c:if test="${requestScope.jwtUser == null}">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}<%= Routes.SIGN_IN_URI %>">
                        S'identifier
                    </a>
                </li>
            </c:if>

            <c:if test="${requestScope.jwtUser != null}">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}<%= Routes.EVENT_CREATE_URI %>">
                        Créer un évènement
                    </a>
                </li>

                <!--<a class="nav-link" href="${pageContext.request.contextPath}<%= Routes.ACCOUNT_DETAILS_URI %>?page=Evenements">
                    Mes évènements
                </a></li> -->

                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown">
                        Mon compte
                    </a>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="${pageContext.request.contextPath}<%= Routes.ACCOUNT_DETAILS_URI %>?page=Informations">
                            Mes informations
                        </a>
                        <a class="dropdown-item" href="${pageContext.request.contextPath}<%= Routes.ACCOUNT_DETAILS_URI %>?page=Evenements">
                            Mes évènements
                        </a>
                        <a class="dropdown-item" href="${pageContext.request.contextPath}<%= Routes.DECO_URI %>">
                            Se déconnecter
                        </a>
                    </div>          
                </li>
            </c:if>

            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}<%= Routes.ABOUT_URI %>">
                    À propos
                </a>
            </li>
        </ul>
    </div>
  </div>
</nav>
