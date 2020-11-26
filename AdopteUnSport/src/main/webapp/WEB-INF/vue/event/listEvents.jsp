<script>
    $(document).ready(function() {
        $('#date').attr('min', new Date().toISOString().split('T')[0]);
    });
</script>

<hr class="mt-1 mb-3" />

<button class="btn btn-info" data-toggle="collapse" data-target="#homeFilters">
    Filtres
</button>

<form method="get" action="${pageContext.request.contextPath}<%= Routes.HOME_URI %>">

<div class="collapse" id="homeFilters" style="margin-left:10%;margin-right:10%;">

    <div class="form-row">
        <div class="form-group col-md-6">
            <label for="address">Lieu</label>
            <input class="form-control text-center" type="text" id="address" name="address"
                placeholder="Exemple : Lyon">
        </div>
    
        <div class="form-group col-md-6">
            <label for="sport">Sport</label>
            <select class="form-control text-center" id="sport" name="sport">
                <c:forEach items='${requestScope.sports}' var="sport">
                    <option value="${sport.name}">${sport.name}</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="form-row">
        <div class="form-group col-md-2">
            <label for="date">Date</label>
            <input class="form-control text-center" type="date" id="date" name="date">
        </div>
    
        <div class="form-group col-md-2">
            <label for="time">Heure</label>
            <input class="form-control text-center" type="time" id="time" name="time">
        </div>
    
        <div class="form-group col-md-2 no_wrap">
            <label class="no_wrap" for="nbMax">Nb max de joueurs</label>
            <input class="form-control text-center" type="number" id="nbMax" name="nbMax" min="2" max="100">
        </div>

        <div class="form-group col-md-3">
            <label for="eventTypes">Type d'évènement</label>
            <select class="form-control text-center" id="eventTypes" name="eventType">
                <option value="Libre" selected>Libre</option>
                <option value="Match">Match</option>
                <option value="Tournoi">Tournoi</option>
            </select>
        </div>
    
        <div class="form-group col-md-3">
            <label for="owner">Organisateur</label>
            <input class="form-control text-center" type="text" id="owner" name="owner">
        </div>
    </div>
             
    <div class="row">
        <div class="col-sm-6 offset-sm-3" align="center">
            <button class="btn btn-lg btn-primary" type="submit" name="listBtn" value="showEvents">Appliquer</button>
        </div>
    </div>

</div>
</form>     



<table class="table table-striped">
    <caption style="caption-side: top;">Liste des évènements</caption>
    <thead class="thead-dark">
        <tr>
            <th scope="col">Sport</th>
            <th scope="col">Organisateur</th>
            <th scope="col">Date</th>
            <th scope="col">Adresse</th>
            <th scope="col">Ville</th>
            <th scope="col">Type Evenement</th>
            <th scope="col">Nb. de joueurs</th>
            <th scope="col">Détails</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items='${requestScope.existingEvents}' var="event">
            <tr>
                <td>${event.sport.name}</td>
                <td>${event.owner.username}</td>
                <td>${event.date}</td>
                <td>${event.address}</td>
                <td>${event.town}</td>
                <td>${event.eventType}</td>
                <td>${event.currentPlayers} / ${event.maxPlayers}</td>
                <td>
                    <form method="get" action="${pageContext.request.contextPath}<%= Routes.EVENT_DETAILS_URI%>">
                        <input type="hidden" id="idEvent" name="idEvent" value="${event.idEvent}">
                        <input class="btn btn-sm btn-primary btn-block align-items-center" type="submit" value="Détails">
                    </form>
                </td>
            </tr>
        </c:forEach>  
    </tbody>
</table>
