<%@ include file="../component/header.jsp" %>

<script src="https://www.google.com/recaptcha/api.js" async defer></script>

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

    <h1>Créer un évènement</h1>

    <hr />

    <div style="margin-left:10%;margin-right:10%;padding:30;border:2px solid gray;">
        <form method="post" action="${pageContext.request.contextPath}<%= Routes.EVENT_CREATE_URI %>">
            <c:if test="${requestScope.successMsg != null}">
                <p style="color:green">${requestScope.successMsg}</p>
            </c:if>
            <c:if test="${requestScope.errorMsg != null}">
                <p style="color:red">${requestScope.errorMsg}</p>
            </c:if>

            <div class="form-row">
                <div class="form-group col-md-7">
                    <label for="address">Adresse*</label>
                                    <i class="far fa-question-circle" aria-hidden="true" title="Indiquez l'adresse exacte où se déroulera l'évènement"></i>
                    <input class="form-control" type="text" id="address" name="address" maxlength="35" required
                                        placeholder="12 rue des fleurs">
                </div>
                <div class="form-group offset-md-1 col-md-4">
                    <label for="city">Ville*</label>
                    <input class="form-control" type="text" id="city" name="city" maxlength="15" required
                                    placeholder="Lyon">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group offset-md-1 col-md-2">
                    <label for="date">Date*</label>
                                    <input class="form-control text-center" type="date" id="date" name="date" required>
                </div>
                <div class="form-group offset-md-1 col-md-2">
                    <label for="time">Heure*</label>
                    <input class="form-control text-center" type="time" id="time" name="time" required>
                </div>
                <div class="form-group offset-md-2 col-md-4">
                    <label for="contact">Contact*</label>
                    <input class="form-control" type="email" id="contact" name="contact" required
                                    placeholder="pierre@contact.fr">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group col-lg-3">
                    <label for="sport">Sport*</label>
                    <select class="form-control text-center" id="sport" name="sport" required>
                        <c:forEach items='${requestScope.sports}' var="sport">
                                <option value="${sport.name}">${sport.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group offset-lg-1 col-lg-4">
                    <label for="eventTypes">Type d'évènement*</label>
                    <select class="form-control text-center" id="eventTypes" name="eventType" required>
                            <option value="Libre" selected>Libre</option>
                            <option value="Match">Match</option>
                            <option value="Tournoi">Tournoi</option>
                    </select>
                </div>
                <div class="form-group offset-lg-1 col-lg-3">
                    <label for="nbMax">Nombre de joueurs*</label>
                    <input class="form-control text-center" type="number" id="nbMax" name="maxPlayers" min="2" max="100" required>
                </div>
            </div>

            <div class="form-group">
                <label for="desc">Description*</label>
                <textarea class="form-control" maxlength="1000" id="desc" minlength="10" name="description" rows="5" placeholder="Informations sur l'évènement" required></textarea>
            </div>

            <div class="form-group row">
                <p class="offset-sm-4 col-sm-2">
                    Allez vous participer ?
                    <i class="far fa-question-circle" class aria-hidden="true" title="Cochez la case si vous allez participer en tant que joueur à l'évènement."></i>
                </p>
                <div class="col-sm-3 form-check">
                    <input class="form-check-input" type="checkbox" name="isOwnerPlaying" checked>
                </div>
            </div>
            <div class="g-recaptcha" data-sitekey="6Le2WOoZAAAAANyO5jktUqL8DRU9IJAPyJa9YKd-"></div>
            <br />

            <button class="btn btn-lg btn-primary" type="submit" name="createBtn" value="createEvent">
            Créer
            </button>

        </form>

        <hr />
        <p>Tu souhaites rejoindre un évènement ?</p>
        <a href="${pageContext.request.contextPath}<%= Routes.HOME_URI %>">Voir la liste des évènements</a>
    </div>

</div>

<%@ include file="../component/footer.jsp" %>