<%@ include file="../component/header.jsp" %>

<head>
    <title>Mon compte</title>
</head>

<div class="text-center">

    <h2>Mon compte</h2>

    <nav class="nav justify-content-center">
        <a class="nav-link" href="?page=Informations">Mes informations</a>
        <a class="nav-link" href="?page=Evenements">Mes évènements</a>
    </nav>

    <hr class="mt-1 mb-3" />

    <div class="account-content">
        <c:choose>
            <c:when test="${param.page eq 'Informations'}">
                <%@include file="information.jsp" %>
            </c:when>
            <c:when test="${param.page eq 'Evenements'}">
                <%@include file="myevents.jsp" %>
            </c:when>
            <c:otherwise>
                <%@include file="information.jsp" %>
            </c:otherwise>
        </c:choose>
    </div>

</div>


<%@ include file="../component/footer.jsp" %>