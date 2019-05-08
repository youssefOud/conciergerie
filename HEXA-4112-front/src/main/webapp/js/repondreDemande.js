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
                                    if (data.uniteDuree = "heures") {
                                        document.getElementById("heures").checked = "";
                                    } else if (data.uniteDuree = "jours") {
                                        document.getElementById("jours").checked = "";
                                    } else if (data.uniteDuree = "minutes") {
                                        document.getElementById("minutes").checked = "";
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
                        
                        var nbPictures = 1;
                
                document.getElementById("ajouterBox").addEventListener("click", function () {
                                    if (nbPictures < 4) {
                            $(this).closest(".row").find('.imgAdd').before('<div class="col-sm-2 imgUp"><div class="imagePreview"></div><label class="btn btn-primary btn-image">Charger<input type="file" class="uploadFile img" value="Upload Photo" style="width:0px;height:0px;overflow:hidden;"></label><i class="fa fa-times del"></i></div>');
                            nbPictures++;
                            } else if (nbPictures == 4) {
                                    $(this).closest(".row").find('.imgAdd').replaceWith('<div class="col-sm-2 imgUp"><div class="imagePreview"></div><label class="btn btn-primary btn-image">Charger<input type="file" class="uploadFile img" value="Upload Photo" style="width:0px;height:0px;overflow:hidden;"></label><i class="fa fa-times del"></i></div>');
                            nbPictures++;
                }
                console.log(nbPictures);
                
                });
                    $(document).on("click", "i.del", function () {
                                    if (nbPictures == 5) {
                            $(this).parent().siblings().last().after('<i class="fa fa-plus imgAdd" id="ajouterBox"></i>');
                            document.getElementById("ajouterBox").addEventListener("click", function () {
                            if (nbPictures < 4) {
                            $(this).closest(".row").find('.imgAdd').before('<div class="col-sm-2 imgUp"><div class="imagePreview"></div><label class="btn btn-primary btn-image">Charger<input type="file" class="uploadFile img" value="Upload Photo" style="width:0px;height:0px;overflow:hidden;"></label><i class="fa fa-times del"></i></div>');
                            nbPictures++;
                    } else if (nbPictures == 4) {
                                    $(this).closest(".row").find('.imgAdd').replaceWith('<div class="col-sm-2 imgUp"><div class="imagePreview"></div><label class="btn btn-primary btn-image">Charger<input type="file" class="uploadFile img" value="Upload Photo" style="width:0px;height:0px;overflow:hidden;"></label><i class="fa fa-times del"></i></div>');
                            nbPictures++;
                        }
                        });
                        }
                        $(this).parent().remove();
                        nbPictures--;
                        });
                        $(function () {
                                    $(document).on("change", ".uploadFile", function () {
                            var uploadFile = $(this);
                            var files = !!this.files ? this.files : [];
                            if (!files.length || !window.FileReader)
                                    return; // no file selected, or no FileReader support

                            if (/^image/.test(files[0].type)) { // only image file
                            var reader = new FileReader(); // instance of the FileReader
                            reader.readAsDataURL(files[0]); // read the local file

                            reader.onloadend = function () { // set image data as background of div
                            //alert(uploadFile.closest(".upimage").find('.imagePreview').length);
                            uploadFile.closest(".imgUp").find('.imagePreview').css("background-image", "url(" + this.result + ")");
                        }
                        }
                        
                        });
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
                            //Description
                            if (document.getElementById("description").value != "") {
                                answers.description = document.getElementById("description").value;
                            }else{
                                answers.description="";
                            }
                            //Localisation
                            answers.localisation = document.getElementById('localisations').value;
                            //Images
                            myArrayFile = $('.uploadFile');
                    answers.pictures="";
                
                for (i = 0; i < myArrayFile.length; i++) {
                                    var uploadFile = myArrayFile[i];
                            var files = !!myArrayFile[i].files ? myArrayFile[i].files : [];
                            if (!files.length || !window.FileReader) {
                            console.log(myArrayFile.length);
                            if (myArrayFile.length > 1){
                            send = false;
                            }
                            break; // no file selected, or no FileReader support
                                }
                            if (/^image/.test(files[0].type)) { // only image file
                                    var reader = new FileReader(); // instance of the FileReader
                            reader.onload = function (event) {
                            // I usually remove the prefix to only keep data, but it depends on your server
                            var data = event.target.result.replace("data:" + files[0].type + ";base64,", "data:" + files[0].type + ";base64, ");
                            answers.pictures += data + "-";
                                };
                                reader.readAsDataURL(files[0]);
                                }
                                }
                            
                            
                            if (send) {
                                //Validation
                                $("#validation").modal();
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
                            }
                        });
                    }
                },
                error(data){
                    console.log(data);
                    $("#erreur").modal();
                }
            });
    
        });