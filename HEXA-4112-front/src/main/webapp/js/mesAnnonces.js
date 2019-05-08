$(document).ready(function () {
            var session = new Object();
            session.todo = "recupererInfoPersonne";
            $.ajax({
            type: 'POST',
                    url: './ActionServlet',
                    data: session,
                    timeout: 3000,
                    dataType:'json',
                    success: function (data) {
                    console.log(data);
                    if (!data.session){
                    window.location.replace(
                            "./index.html");
            }
            else{
                         document.getElementById("bonjour").innerHTML="Bonjour, "+ data.prenom;
                         document.getElementById("deco").addEventListener("click", function () {
                        var deco=new Object();
                        deco.todo="seDeconnecter";
                    $.ajax({
                    type: 'POST',
                            url: './ActionServlet',
                            data: deco,
                            timeout: 3000,
                            dataType:'json',
                            success: function (data) {
                            console.log(data);
                            if (data.deconnexion){
                            window.location.replace(
                                    "./index.html");
            }
            else{

                                    $("#erreur").modal();
                    }
                    },
                    error: function (data) {
                    console.log(data);
                                    $("#erreur").modal();
                }
                });
                });
                
                var essai = new Object();
                    essai.todo="getAnnoncesPersonne";
                    $.ajax({
                            type: 'POST',
                                    url: './ActionServlet',
                                    data: essai,
                                    timeout: 3000,
                                    dataType:'json',
                                    success: function (data) {
                                        console.log(data);
                                        $('#fil').empty();
                                        if(data.Annonces.length==0){
                                            $('#fil').append('<p>Aucune annonce ne correspond à votre recherche</p>')
                                        }
                                        for (var i in data.Annonces)
                                        {
                                            var annonce = '<div class="card mb-3"><h3 class="card-header">'
                                            if(data.Annonces[i].etat != "closed"){
                                                annonce+='<button class="btn"\n\
                                                onclick="'+"$.ajax({type: 'POST',url: './ActionServlet',data: { todo : 'supprimerAnnonce', idAnnonce :"+data.Annonces[i].idAnnonce+"},dataType:'json',success: function (data) {console.log(data);if (data.supprime){window.location.reload();}else{$('#erreur').modal();}},error: function () {$('#erreur').modal();}});"+'"';
                                                annonce+='><i class="fa fa-trash-o fa-2x" style="color:black" ></i></button>';
                                            }
                                            
                                            if (data.Annonces[i].typeAnnonce == "offre"){
                                                annonce += '<span style="color:#007bff" >OFFRE</span>';
                                            }
                                            else if (data.Annonces[i].typeAnnonce == "demande"){
                                                annonce += '<span style="color:#f47d42" >DEMANDE</span>';
                                            }
                                            annonce += ' - Prêt - ' + data.Annonces[i].objet + '<div class="text-right">';
                                            if (data.Annonces[i].etat == 0){
                                                annonce += '<span style="color:#4da026" >EN COURS</span>';
                                            }
                                            else if (data.Annonces[i].etat == 2){
                                                annonce += '<span style="color:#9b2699" >FERMEE</span>';
                                            }
                                            else if (data.Annonces[i].etat == 1){
                                                annonce += '<span style="color:#f44253" >EXPIREE</span>';
                                            }
                                            annonce += '</div></h3><div class="card-body"><h6 class="card-subtitle text-muted">' + data.Annonces[i].categorie + '</h6>';
                                            if (data.Annonces[i].pictures != undefined && data.Annonces[i].pictures.length != 0){
                                                annonce += '<div id="carousel' + i + '" class="carousel slide" data-ride="carousel">\n\
                                                            <div class="carousel-inner">\n\
                                                            <div class="carousel-item active">\n\
                                                        <img class="d-block w-100 center"  style="max-width:50%;max-height:20%" src="' + data.Annonces[i].pictures[0] + '" alt="First slide"></div>';
                                                for (var j = 1; j < data.Annonces[i].pictures.length; j++){
                                                    annonce += '<div class="carousel-item">\n\
                                                    <img class="d-block w-100 center" style="max-width:50%;max-height:20%" src="' + data.Annonces[i].pictures[j] + '" alt="Second slide">\n\
                                                    </div>';
                                                }
                                                annonce += '</div>\n\
                                                <a class="carousel-control-prev" href="#carousel' + i + '" role="button" data-slide="prev">\n\
                                                            <span class="carousel-control-prev-icon" aria-hidden="true"></span>\n\
                                                    <span class="sr-only">Previous</span>\n\
                                                </a>\n\
                                                <a class="carousel-control-next" href="#carousel' + i + '" role="button" data-slide="next">\n\
                                                <span class="carousel-control-next-icon" aria-hidden="true"></span>\n\
                                                <span class="sr-only">Next</span>\n\
                                                </a>\n\
                                                </div>';
                                            }
                                            else{
                                                annonce += '<img class="d-block w-100 center" style="max-width:50%;max-height:20%" src="http://cliquecities.com/assets/no-image-e3699ae23f866f6cbdf8ba2443ee5c4e.jpg" >';
                                            }
                                            annonce += '</div><div><div class="card"><div class="card-header">\n\
                                            <h5 class="mb-0">\n\
                                                <button class="btn btn-link" data-toggle="collapse" data-target="#collapse' + i + '" aria-expanded="true" aria-controls="collapse' + i + '" id="accordion' + i + '">\n\
                                            Détails\n\
                                            </button>\n\
                                                </h5>\n\
                                                    </div>\n\
                                                    <div id="collapse' + i + '" class="collapse" aria-labelledby="headingOne" data-parent="#accordion' + i + '">';
                                                    
                                            if (data.Annonces[i].description != undefined && data.Annonces[i].description != ""){
                                            annonce += '<div class="card-body">' + data.Annonces[i].description + '</div>';
                                            }

                                            annonce += '<ul class="list-group list-group-flush"><li class="list-group-item">Date de disponibilité : ' + data.Annonces[i].date + ' ' + data.Annonces[i].time + '</li>\n\
                                                <li class="list-group-item">Durée de disponibilité : ' + data.Annonces[i].duree + ' ' + data.Annonces[i].uniteDuree + '</li>\n\
                                            <li class="list-group-item">Prix : ' + data.Annonces[i].nbPts + ' points par ' + data.Annonces[i].unitePrix + '</li>\n\
                                                <li class="list-group-item">Localisation : ' + data.Annonces[i].localisation + '</li>\n\
                                                </ul></div></div>';
                                            
                                            
                                            
                                            
                                            annonce +='<div><div class="card"><div class="card-header">\n\
                                            <h5 class="mb-0">\n\
                                                <button class="btn btn-link" data-toggle="collapse" data-target="#rep' + i + '" aria-expanded="true" aria-controls="collapse' + i + '" id="parent' + i + '">\n\
                                            Réponses\n\
                                            </button>\n\
                                                </h5>\n\
                                                    </div>\n\
                                                    <div id="rep' + i + '" class="collapse" aria-labelledby="headingOne" data-parent="#parent' + i + '">';
                                                    
                                            if (data.Annonces[i].reponses != undefined ){
                                                annonce+='<br/><table id="reponses'+i+'" class="table table-striped table-bordered table-sm" cellspacing="0" width="100%">\n\
                                                            <thead>\n\
                                                              <tr>\n\
                                                                <th class="th-sm">Action</th>\n\
                                                                <th class="th-sm">Contact\n\
                                                                </th>\n\
                                                                <th class="th-sm">Date\n\
                                                                </th>\n\
                                                                <th class="th-sm">Durée\n\
                                                                </th>\n\
                                                                <th class="th-sm">Prix\n\
                                                                </th>\n\
                                                                <th class="th-sm">Note\n\
                                                                </th>\n\
                                                              </tr>\n\
                                                            </thead>\n\
                                                            <tbody>';
                                                for (var j in data.Annonces[i].reponses){
                                                    
                                                    if(data.Annonces[i].reponses[j].etat == 0){//encours
                                                        annonce+='<tr><td><button class="btn"\n\
                                                        onclick="'+"$.ajax({type: 'POST',url: './ActionServlet',data: { todo : 'validerReponseAnnonce', idReservation :"+data.Annonces[i].reponses[j].idReponse+"},dataType:'json',success: function (data) {console.log(data);if (data.confirmationDone){window.location.reload();}else{$('#erreur').modal();}},error: function () {$('#erreur').modal();}});"+'"';
                                                        annonce+='><i class="fa fa-check"></i></button><button class="btn"\n\
                                                        onclick="'+"$.ajax({type: 'POST',url: './ActionServlet',data: { todo : 'declinerReponseAnnonce', idReservation :"+data.Annonces[i].reponses[j].idReponse+"},dataType:'json',success: function (data) {console.log(data);if (data.declined){window.location.reload();}else{$('#erreur').modal();}},error: function () {$('#erreur').modal();}});"+'"';
                                                        annonce+=' ><i class="fa fa-times"></i></button></td>';
                                                    }//acceptée
                                                    else if(data.Annonces[i].reponses[j].etat == 1){
                                                        annonce+='<tr class="table-success"><td></td>';
                                                    }//refusée
                                                    else if(data.Annonces[i].reponses[j].etat == 2){
                                                        annonce+='<tr class="table-danger"><td></td>';
                                                    }//terminée
                                                    else if(data.Annonces[i].reponses[j].etat == 3 || data.Annonces[i].reponses[j].etat == 5){
                                                        annonce+='<tr><td><input type="number" min="0" max="5" value="0" id="rating"/><button class="btn" \n\
                                                        onclick="'+"var rating=$('#rating').val();$.ajax({type: 'POST',url: './ActionServlet',data: { todo : 'noterBeneficiaire', reservationId :"+data.Annonces[i].reponses[j].idReponse+", rating : rating },dataType:'json',success: function (data) {console.log(data);if (data.note){window.location.reload();}else{$('#erreur').modal();}},error: function () {$('#erreur').modal();}});"+'"';
                                                        annonce+='><i class="fa fa-star"></i></button></td>';
//                                                            annonce+='<tr><td></td>'
                                                    }//notée
                                                    else if(data.Annonces[i].reponses[j].etat == 4 || data.Annonces[i].reponses[j].etat == 6  ){
                                                        annonce+='<tr class="table-active"><td>Prêt noté</td>';
                                                    }
                                                                
                                                    annonce+='<td>'+data.Annonces[i].reponses[j].auteur+'</td>\n\
                                                                <td>'+data.Annonces[i].reponses[j].date+' '+data.Annonces[i].reponses[j].time+'</td>\n\
                                                                <td>'+data.Annonces[i].reponses[j].duree+' '+data.Annonces[i].reponses[j].uniteDuree+'</td>\n\
                                                                <td>'+data.Annonces[i].reponses[j].prix+' points</td>';
                                                    if(data.Annonces[i].reponses[j].note != -1){
                                                        annonce+='<td>'+data.Annonces[i].reponses[j].note+'/5 </td>';
                                                    }
                                                    else{
                                                        annonce+='<td>Non noté</td>';
                                                    }
                                                                
                                                    annonce+='</tr>';
                                                            
                                                }
                                                annonce+='</tbody></table><br/>';
                                            
                                            }
                                            annonce += '</div></div>';
                    
                    
                    
                    
                                            annonce += '<div class="card-footer text-muted">Contact : '+data.Annonces[i].auteur +'<br/>Le ' + data.Annonces[i].datePublication + ' à ' + data.Annonces[i].timePublication + '</div></div></div></div>'
                                            $('#fil').append(annonce);
                                            console.log('#reponses'+i)
                                            $('#reponses'+i).DataTable({"language": 
                                            {
                                                    "sProcessing":     "Traitement en cours...",
                                                    "sSearch":         "Rechercher&nbsp;:",
                                                "sLengthMenu":     "Afficher _MENU_ &eacute;l&eacute;ments",
                                                    "sInfo":           "Affichage de l'&eacute;l&eacute;ment _START_ &agrave; _END_ sur _TOTAL_ &eacute;l&eacute;ments",
                                                    "sInfoEmpty":      "Affichage de l'&eacute;l&eacute;ment 0 &agrave; 0 sur 0 &eacute;l&eacute;ment",
                                                    "sInfoFiltered":   "(filtr&eacute; de _MAX_ &eacute;l&eacute;ments au total)",
                                                    "sInfoPostFix":    "",
                                                    "sLoadingRecords": "Chargement en cours...",
                                                "sZeroRecords":    "Aucune réponse à afficher",
                                                    "sEmptyTable":     "Aucune réponse trouvée",
                                                    "oPaginate": {
                                                            "sFirst":      "Premier",
                                                            "sPrevious":   "Pr&eacute;c&eacute;dent",
                                                            "sNext":       "Suivant",
                                                            "sLast":       "Dernier"
                                                    },
                                                    "oAria": {
                                                            "sSortAscending":  ": activer pour trier la colonne par ordre croissant",
                                                            "sSortDescending": ": activer pour trier la colonne par ordre d&eacute;croissant"
                                                    },
                                                    "select": {
                                                            "rows": {
                                                                    _: "%d lignes séléctionnées",
                                                                    0: "Aucune ligne séléctionnée",
                                                                    1: "1 ligne séléctionnée"
                                                            }  
                                                    }
                                            }
                                        });
                                        $('.dataTables_length').addClass('bs-select');
                                    }
                            
                            },
                        error: function (data) {
                            console.log(data);
                            $("#erreur").modal();
                        }
                    });
                }
                },
                error: function (data) {
                    console.log(data);
                                    $("#erreur").modal();
                }
                });
            });