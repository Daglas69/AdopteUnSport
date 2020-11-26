<%@ include file="../component/header.jsp"%>

<head>
    <title>Detail de l'évènement</title>
</head>

<h1 class="text-center">Information d'un évènement</h1>

<hr />
<div style="margin-left:10%;margin-right:10%;padding:30;border:2px solid gray;">
    
    <div class="text-center">
        <c:if test="${eventdto.cancelled}">
            <h3 style="color:darkred">  Évènement annulé par l'organisateur ! </h3>
        </c:if>

        <c:if test="${requestScope.successMsg != null}">
            <p style="color:green">${requestScope.successMsg}</p>
        </c:if>
        <c:if test="${requestScope.errorMsg != null}">
            <p style="color:red">${requestScope.errorMsg}</p>
        </c:if>
    <div>
    
    <div class="form-row">
        <div class="form-group col-md-7">
            <label for="address">Adresse</label>
            <input class="form-control" type="text" id="address" name="address"
                   value="${eventdto.address}" disabled>
        </div>
        <div class="form-group offset-md-1 col-md-4">
            <label for="city">Ville</label>
            <input class="form-control" type="text" id="city" name="city" 
                       value="${eventdto.town}" disabled>
        </div>
    </div>

    <div class="form-row">
	<div class="form-group offset-md-1 col-md-2">
            <label for="date">Date</label>
            <input class="form-control text-center" type="text" id="date" name="date"
		value="${eventdto.date}" disabled>
        </div>
        <div class="form-group offset-md-1 col-md-2">
            <label for="time">Heure</label>
            <input class="form-control text-center" type="time" id="time" name="time"
                value="${eventdto.hour}" disabled>
        </div>
	<div class="form-group offset-md-2 col-md-4">
            <label for="contact">Contact</label>
            <input class="form-control" type="text" id="contact" name="contact" 
		value="${eventdto.contact}" disabled>
        </div>
    </div>

    <div class="form-row">
	<div class="form-group col-lg-3">
            <label for="sport">Sport</label>
            <input class="form-control text-center" type="text" id="sport" name="sport"
               value="${eventdto.sport.name}" disabled>
        </div>
	<div class="form-group offset-lg-1 col-lg-4">
            <label for="eventTypes">Type d'évènement</label>
            <input class="form-control text-center" type="text" id="eventTypes" name="eventType" 
                       value="${eventdto.eventType}" disabled>
        </div>
	<div class="form-group offset-lg-1 col-lg-3">
            <label for="nbMax">Nombre de joueurs</label>
            <input class="form-control text-center" type="text" id="nbMax" name="currentPlayers" min="0" 
                   value="${eventdto.currentPlayers}" disabled>
            <label for="nbMax">Maximum de joueurs</label>
            <input class="form-control text-center" type="text" id="nbMax" name="maxPlayers" min="2" 
                   value="${eventdto.maxPlayers}" disabled>
        </div>
    </div>

    <div class="form-group">
   	<label for="desc">Description</label>
        <textarea class="form-control" maxlength="1000" id="desc" name="description" rows="5" disabled>${eventdto.description}</textarea>
    </div>
    <br />
</div>
	
<hr />

<div class="row mb-3">
    <c:choose>
        <c:when test="${jwtUser == null}">
            <div class="col-md-6 offset-md-3" align="center">
                <p> Vous devez être connecté pour pouvoir intéragir avec l'évènement </p>
                <p><a href="../connexion">Se connecter</a></p>
            </div>
        </c:when>

        <c:when test="${eventdto.terminated || eventdto.cancelled}">
            <!-- We do not display anything sir -->
        </c:when>

        <c:when test="${requestScope.currentUserOwner}">
            <div class="col-md-2 offset-md-4">
                <form method="get" action="${pageContext.request.contextPath}<%= Routes.EVENT_MODIF_URI%>">
                    <input type="hidden" id="idEvent" name="idEvent" value="${eventdto.idEvent}">
                    <input class="btn btn-md btn-primary btn-block" type="submit" value="Modifier">
                </form>
            </div>
            <div class="col-md-2">
                <form method="post" action="${pageContext.request.contextPath}<%= Routes.EVENT_SUPPR_URI%>">
                    <input type="hidden" id="idEvent" name="idEvent" value="${eventdto.idEvent}">
                    <input class="btn btn-md btn-danger btn-block" type="submit" value="Supprimer">
                </form>
            </div>
        </c:when>

        <c:when test="${requestScope.currentUserPlayer}">
            <div class="col-md-2 offset-md-5">
                <form method="post" action="${pageContext.request.contextPath}<%= Routes.EVENT_QUIT_URI%>">
                    <input type="hidden" id="idEvent" name="idEvent" value="${eventdto.idEvent}">
                    <input type="hidden" id="email" name="email" value="${jwtUserEmail}">
                    <input class="btn btn-md btn-danger btn-block" type="submit" value="Quitter">
                </form>
            </div>
        </c:when>

        <c:otherwise>
            <div class="col-md-2 offset-md-5">
                <form method="post" action="${pageContext.request.contextPath}<%= Routes.EVENT_JOIN_URI%>">
                    <input type="hidden" id="idEvent" name="idEvent" value="${eventdto.idEvent}">
                    <input type="hidden" id="email" name="email" value="${jwtUserEmail}">
                    <input class="btn btn-md btn-success btn-block" type="submit" value="Rejoindre">
                </form>
            </div>
        </c:otherwise>
    </c:choose>
</div>



<form method="get" action="${pageContext.request.contextPath}<%= Routes.HOME_URI %>">
    <div class="row mb-2">
        <div class="col-md-2">
            <button class="btn btn-sm btn-info" type="submit"> < Accueil </button>
        </div>
    </div>
</form>


<%@ include file="../component/footer.jsp" %>