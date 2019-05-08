$(document).ready(function () {
            var session = new Object();
            session.todo = "recupererInfoPersonne";
            $.ajax({
                type: 'POST',
                url: './ActionServlet',
                data: session,
                timeout: 3000,
                dataType: 'json',
                success: function(data) {
                    console.log(data);
                    if (!data.session) {
                        window.location.replace(
                            "./index.html");
                    } else {
                        document.getElementById("bonjour").innerHTML = "Bonjour, " + data.prenom;
                        document.getElementById("deco").addEventListener("click", function() {
                            var deco = new Object();
                            deco.todo = "seDeconnecter";
                            $.ajax({
                                type: 'POST',
                                url: './ActionServlet',
                                data: deco,
                                dataType: 'json',
                                success: function(data) {
                                    console.log(data);
                                    if (data.deconnexion) {
                                        window.location.replace(
                                            "./index.html");
                                    } else {

                                        $("#erreur").modal();
                                    }
                                },
                                error: function(data) {
                                    console.log(data);
                                    $("#erreur").modal();
                                }
                            });
                        });
                        $('#date').datepicker({
                            format: "dd/mm/yyyy",
                            locale: "fr",
                            autoclose: true,
                            todayHighlight: true,
                            pick12HourFormat: true
                        });
                        $('#time').clockpicker({
                            autoclose: true,
                            donetext: 'Done'
                        });
                        var details = new Object();
                        details.todo = "detailsAnnonce";
                        var url = new URL(window.location);
                        details.idAnnonce = url.searchParams.get("idAnnonce");
                        $.ajax({
                            type: 'POST',
                            url: './ActionServlet',
                            data: details,
                            timeout: 3000,
                            dataType: 'json',
                            success: function(data) {
                                console.log(data);
                                if (data.annonce) {
                                    var titre = "Réponse à une ";
                                    if (data.typeAnnonce == "offre") {
                                        titre += '<span style="color:#007bff" >OFFRE</span>';
                                    } else if (data.typeAnnonce == "demande") {
                                        titre += '<span style="color:#f47d42" >DEMANDE</span>';
                                    }
                                    titre += "<br/> Prêt - " + data.objet;
                                    document.getElementById("titre").innerHTML = titre;

                                    document.getElementById("date").value = data.date;    
                                        $('#date').datepicker('setDate', data.date.replace('/','-'));
   
                                    
                                    document.getElementById("duree").value = data.duree;
                                    document.getElementById("time").value = data.time;
                                    if (data.uniteDuree == "heures") {
                                        $("#heures").prop("checked", true);
                                    } else if (data.uniteDuree == "jours") {
                                        $("#jours").prop("checked", true);
                                    } else if (data.uniteDuree == "minutes") {
                                        $("#minutes").prop("checked", true);
                                    }

                                } else {

                                    $("#erreur").modal();
                                }
                            },
                            error: function(data) {
                                console.log(data);
                                $("#erreur").modal();
                            }
                        });
                        document.getElementById("calculerPrix").addEventListener("click", function() {
                            var send = true;
                            var calculPrix = new Object();
                            calculPrix.todo = "calculPrix";
                            var url = new URL(window.location);
                            calculPrix.idAnnonce = url.searchParams.get("idAnnonce");
                            if (document.getElementById("duree").value != "") {
                                calculPrix.duree = document.getElementById("duree").value;
                                document.getElementById("duree").classList.remove("is-invalid");
                            } else {
                                send = false;
                                document.getElementById("duree").classList.add("is-invalid");
                            }

                            //Unité durée
                            calculPrix.uniteDuree = "heures";
                            if ($('#jours').is(':checked')) {
                                calculPrix.uniteDuree = "jours";
                            } else if ($('#minutes').is(':checked')) {
                                calculPrix.uniteDuree = "minutes";
                            }
                            if (send) {
                            
                                $.ajax({
                                    type: 'POST',
                                    url: './ActionServlet',
                                    data: calculPrix,
                                    timeout: 3000,
                                    dataType: 'json',
                                    success: function(data) {
                                        console.log(data);
                                        if (data.calcule) {
                                            document.getElementById("prixCalcule").value = data.prix;
                                        } else {

                                            $("#erreur").modal();
                                        }
                                    },
                                    error: function(data) {
                                        console.log(data);
                                        $("#erreur").modal();
                                    }
                                });
                            }
                        });
                        
                        document.getElementById("valider").addEventListener("click", function() {
                            var send = true;
                            var answers = new Object();
                            var url = new URL(window.location);
                            answers.idAnnonce = url.searchParams.get("idAnnonce");
                            answers.todo = "repondreAnnonce";
                            //Date
                            if (document.getElementById("date").value != "" && document.getElementById("time").value != "") {
                                answers.date = document.getElementById("date").value;
                                answers.time = document.getElementById("time").value;
                            } else if (document.getElementById("date").value == "") {
                                send = false;
                                document.getElementById("date").classList.add("is-invalid");
                            }
                            if (document.getElementById("time").value == "") {
                                send = false;
                                document.getElementById("time").classList.add("is-invalid");
                            }
                            //Durée
                            if (document.getElementById("duree").value != "") {
                                answers.duree = document.getElementById("duree").value;
                                document.getElementById("duree").classList.remove("is-invalid");
                            } else {
                                send = false;
                                document.getElementById("duree").classList.add("is-invalid");
                            }
                            //Unité durée
                            answers.uniteDuree = "heures";
                            if ($('#jours').is(':checked')) {
                                answers.uniteDuree = "jours";
                            } else if ($('#minutes').is(':checked')) {
                                answers.uniteDuree = "minutes";
                            }
                            if (send) {
                                //Validation
                                $("#validation").modal();
                                
                            }
                        });
                    }
                },
                error(data){
                    console.log(data);
                    $("#erreur").modal();
                }
            });
            document.getElementById("deposer").addEventListener("click", function() {
                                    var send = true;
                                    var answers = new Object();
                                    var url = new URL(window.location);
                                    answers.idAnnonce = url.searchParams.get("idAnnonce");
                                    answers.todo = "repondreAnnonce";
                                    answers.date = document.getElementById("date").value;
                                    answers.time = document.getElementById("time").value.substring(0,5);
                                    answers.duree = document.getElementById("duree").value;
                                    answers.uniteDuree = "heures";
                                    if ($('#jours').is(':checked')) {
                                        answers.uniteDuree = "jours";
                                    } else if ($('#minutes').is(':checked')) {
                                        answers.uniteDuree = "minutes";
                                    }
                                    console.log(answers);
                                    $.ajax({
                                        type: 'POST',
                                        url: './ActionServlet',
                                        data: answers,
                                        dataType: 'json',
                                        success: function(data) {
                                            console.log(data);
                                            if (data.creationReponse) {
                                                window.location.replace(
                                                    "./filActu.html?reponse");
                                            } else {
                                                document.getElementById("messageErreur").innerHTML= data.messageErreur;
                                                $("#erreurSpecifique").modal();
                                            }
                                        },
                                        error: function(data) {
                                            console.log(data)
                                            document.getElementById("messageErreur").innerHTML= data.messageErreur;
                                                $("#erreurSpecifique").modal();
                                        }
                                    });
                                });
    
        });