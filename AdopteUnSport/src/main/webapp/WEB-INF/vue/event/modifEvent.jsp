<%@ include file="../component/header.jsp" %>

<script>
    $(document).ready(function() {
        var tomorrow = new Date();
        tomorrow.setDate(new Date().getDate()+1);
        $('#date').attr('min', tomorrow.toISOString().split('T')[0]);
    });
</script>

<head>
    <title>Créer un évènement</title>
</head>

<div class="text-center"> 

    <h1>Modifier un évènement</h1>

    <hr />

    <div style="margin-left:10%;margin-right:10%;padding:30;border:2px solid gray;">
        <form method="post" action="${pageContext.request.contextPath}<%= Routes.EVENT_MODIF_URI %>">
        <input type="hidden" id="idEvent" name="idEvent" value="${eventdto.idEvent}">

        <c:if test="${requestScope.successMsg != null}">
            <p style="color:green">${requestScope.successMsg}</p>
        </c:if>
        <c:if test="${requestScope.errorMsg != null}">
            <p style="color:red">${requestScope.errorMsg}</p>
        </c:if>

        <div class="form-row">
            <div class="form-group col-md-7">
                <label for="address">Adresse</label>
                <input class="form-control" type="text" id="address" name="address" maxlength="35"
                    value="${eventdto.address}" required>
            </div>
            <div class="form-group offset-md-1 col-md-4">
                <label for="city">Ville</label>
                <input class="form-control" type="text" id="city" name="city" maxlength="15"
                    value="${eventdto.town}" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group offset-md-1 col-md-2">
                <label for="date">Date</label>
                <input class="form-control text-center" type="date" id="date" name="date"
                    value="${eventdto.date}" required>
                <i class="far fa-question-circle" aria-hidden="true" title="Indiquez l'adresse exacte où se déroulera l'évènement"></i>
            </div>
            <div class="form-group offset-md-1 col-md-2">
                <label for="time">Heure</label>
                <input class="form-control text-center" type="time" id="time" name="time"
                    value="${eventdto.hour}" required>
            </div>
            <div class="form-group offset-md-2 col-md-4">
                <label for="contact">Contact</label>
                <input class="form-control" type="email" id="contact" name="contact" 
                    value="${eventdto.contact}" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-lg-3">
                <label for="sport">Sport</label>
                <select class="form-control text-center" id="sport" name="sport" required>
                    <c:forEach items='${requestScope.sports}' var="sport">
                        <c:if test="${sport.name.equals(eventdto.sport.name)}">
                            <option value="${sport.name}" selected>${sport.name}</option>
                        </c:if>
                        <c:if test="${!sport.name.equals(eventdto.sport.name)}">
                            <option value="${sport.name}">${sport.name}</option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group offset-lg-1 col-lg-4">
                <label for="eventTypes">Type d'évènement</label>
                <select class="form-control text-center" id="eventTypes" name="eventType" required>
                    <c:if test="${eventdto.eventType.equals(\"Libre\")}">
                        <option value="Libre" selected>Libre</option>
                        <option value="Match">Match</option>
                        <option value="Tournoi">Tournoi</option>
                    </c:if>
                    <c:if test="${eventdto.eventType.equals(\"Match\")}">
                        <option value="Libre">Libre</option>
                        <option value="Match" selected>Match</option>
                        <option value="Tournoi">Tournoi</option>
                    </c:if>
                    <c:if test="${eventdto.eventType.equals(\"Tournoi\")}">
                        <option value="Libre">Libre</option>
                        <option value="Match">Match</option>
                        <option value="Tournoi" selected>Tournoi</option>
                    </c:if>        
                </select>
            </div>
            <div class="form-group offset-lg-1 col-lg-3">
                <label for="nbMax">Maximum de joueurs</label>
                <input class="form-control text-center" type="number" id="nbMax" name="maxPlayers" min="2" max="100"
                    value="${eventdto.maxPlayers}" required>
            </div>
        </div>

        <div class="form-group">
            <label for="desc">Description</label>
            <textarea class="form-control" minlength="10" maxlength="1000" id="desc" name="description" rows="5" required>${eventdto.description}</textarea>
        </div>
        <div class="form-group row">
            <p class="offset-sm-4 col-sm-2">
                Allez vous participer ?
                <i class="far fa-question-circle" aria-hidden="true" title="Cochez la case si vous allez participer en tant que joueur à l'évènement."></i>
            </p>
            <div class="col-sm-3 form-check">
                <c:choose>
                    <c:when test="${requestScope.userIsPlaying}">
                        <input class="form-check-input" type="checkbox" name="isOwnerPlaying" checked>
                    </c:when>
                    <c:otherwise>
                        <input class="form-check-input" type="checkbox" name="isOwnerPlaying">
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="row">
            <div class="col-md-2 offset-md-5">
                <button class="btn btn-md btn-success btn-block" type="submit" name="modifBtn" value="modifEvent">Appliquer</button>
            </div>
        </div>

        </form>

        <div class="row">
            <div class="col-md-2">
                <form method="get" action="${pageContext.request.contextPath}<%= Routes.EVENT_DETAILS_URI%>">
                    <input type="hidden" id="idEvent" name="idEvent" value="${eventdto.idEvent}">
                    <input class="btn btn-sm btn-info btn-block" type="submit" value="< Retour">
                </form>
            </div>
        </div>

        <br />
    </div>

    <hr />
    <p>Tu souhaites rejoindre un évènement ?</p>
    <a href="${pageContext.request.contextPath}<%= Routes.HOME_URI %>">Voir la liste des évènements</a>
</div>



<%@ include file="../component/footer.jsp" %>