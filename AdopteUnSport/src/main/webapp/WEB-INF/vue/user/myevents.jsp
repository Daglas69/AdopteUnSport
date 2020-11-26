<h4> Mes évènements </h4>


<button class="btn btn-info" data-toggle="collapse" data-target="#accountFilters">
  Filtres
</button>

<form method="get" action="${pageContext.request.contextPath}<%= Routes.ACCOUNT_DETAILS_URI %>">
    <input type="hidden" name="page" value="Evenements" />

    <!-- Tricky : We set to "show" the collapse canvas with filters only when user already applied some -->
    <div ${param.afficher != null ? 'class="collapse show"' : 'class="collapse"'} id="accountFilters" style="margin-left:10%;margin-right:10%;">

        <div class="form-row">
            <div class="form-group col-lg-3 offset-lg-3" align="center">
                <label for="afficher">Afficher les évènements</label>
                <select class="form-control" name="afficher" id="afficher">
                    <option value="all" <c:if test='${param.afficher == "all"}'> selected </c:if> > Tous </option>
                    <option value="player" <c:if test='${param.afficher == "player"}'> selected </c:if> > où je participe </option>
                    <option value="owner" <c:if test='${param.afficher == "owner"}'> selected </c:if> > que j'organise </option>
                </select>
            </div>
            <div class="form-group col-lg-3" align="center">
                <label for="triDate">Trier par date</label>
                <select class="form-control" name="triDate" id="triDate">
                    <option value="croissant" <c:if test='${param.triDate == "croissant"}'> selected </c:if> > Ordre croissant </option>
                    <option value="decroissant" <c:if test='${param.triDate == "decroissant"}'> selected </c:if> > Ordre décroissant </option>
                </select>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group offset-md-3 col-md-6" align="center">
                <label for="old">Afficher les évènements terminés :  </label>
                <input type="checkbox" id="old" name="old" <c:if test='${param.old != null}'> checked </c:if> >
            </div>
        </div>

        <div class="form-row">
            <div class="offset-md-5 col-md-2" align="center">
                <input class="btn btn-sm btn-primary btn-block" type="submit" value="Appliquer">
            </div>
        </div>

    </div>

</form>


<table class="table table-striped">
    <caption style="caption-side: top;">Liste des évènements rejoints</caption>
    <thead class="thead-dark">
        <tr>
            <th scope="col">État</th>
            <th scope="col">Sport</th>
            <th scope="col">Organisateur</th>
            <th scope="col">Date</th>
            <th scope="col">Lieu</th>
            <th scope="col">Type Evenement</th>
            <th scope="col">Nb. de joueurs</th>
            <th scope="col">Détails</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items='${requestScope.myEvents}' var="event">
            <tr>
                <td>
                    <c:choose>
                        <c:when test="${event.cancelled}">
                            <p style="color: darkred">Annulé</p>
                        </c:when>
                        <c:when test="${!event.terminated}">
                            <p style="color: darkblue">À venir</p>
                        </c:when>
                        <c:otherwise>
                            <p style="color: darkgrey">terminé</p>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>${event.sport.name}</td>
                <td>${event.owner.username}</td>
                <td>${event.date}</td>
                <td>${event.address}</td>
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