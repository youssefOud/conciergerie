var numTelephone;
            var contactPrefere;

            $(document).ready(function () {
                $('#radioBtn a').on('click', function () {
                    var sel = $(this).data('title');
                    var tog = $(this).data('toggle');
                    $('#' + tog).prop('value', sel);
                    $('a[data-toggle="' + tog + '"]').not('[data-title="' + sel + '"]').removeClass('active').addClass('notActive');
                    $('a[data-toggle="' + tog + '"][data-title="' + sel + '"]').removeClass('notActive').addClass('active');
                });

                var filtres = new Object();
                filtres.todo = "recupererInfoPersonne";

                $.ajax({
                    type: 'GET',
                    url: './ActionServlet',
                    data: filtres,
                    timeout: 3000,
                    dataType: 'json',
                    success: function (data) {
                        if (!data.session){
                            window.location.replace("./index.html");
                        }else{
                            document.getElementById("deco").addEventListener("click", function () {
                                var deco = new Object();
                                deco.todo = "seDeconnecter";
                                $.ajax({
                                    type: 'POST',
                                    url: './ActionServlet',
                                    data: deco,
                                    timeout: 3000,
                                    dataType:'json',
                                    success: function (data) {
                                        console.log(data);
                                        if (!data.error){
                                            window.location.replace("./index.html");
                                        }else{
                                            $("#erreur").modal();
                                        }
                                    },
                                    error: function (data) {
                                        console.log(data);
                                        $("#erreur").modal();
                                    }
                                });
                            });
                            console.log(data);
                            document.getElementById("welcome").innerHTML = "Bonjour "+data.prenom;
                            document.getElementById("nom").innerHTML = data.nom;
                            document.getElementById("prenom").innerHTML = data.prenom;
                            document.getElementById("email").innerHTML = data.email;
                            document.getElementById("mon_profile").innerHTML = data.prenom + " " + data.nom;
                            if(data.note != -1){
                                    document.getElementById("note").innerHTML = data.note + "/5";
                            }else{
                                    document.getElementById("note").innerHTML = "Non noté";
                            }
                            document.getElementById("points").innerHTML = data.nbPoint + " points";
                            numTelephone = data.numTel;
                            contactPrefere = data.contactPrefere;
                            if (data.contactPrefere == "cellphone") {
                                var radio_tel = document.getElementById("radio-tel");
                                radio_tel.className = "btn btn-primary btn-sm active";
                                document.getElementById("radio-email").className = "btn btn-primary btn-sm notActive";
                                OnChangeRadio(radio_tel);
                                document.getElementById("tel").value = data.numTel;
                            }
                            document.getElementById("btnValider").disabled = true;
                        }
                    },
                    error: function (data) {
                        console.log(data);
                        alert("Le changement n'a pu être réalisé, veuillez réessayer plus tard");
                    }
                });
            });

            var last_sel = "email";
            var new_sel;

            function OnChangeRadio(radio) {
                new_sel = $(radio).data('title');
                var tel = document.getElementById("telephone-cont");
                if (new_sel == "telephone" && last_sel == "email") {
                    tel.innerHTML += '<div class="col-md-6"><label>Numéro de téléphone</label></div>';
                    tel.innerHTML += '<div class="col-md-6"><input type="tel" class="form-control bfh-phone" id="tel" value="' + numTelephone + '"></div></div>';
                } else if (new_sel == "email" && last_sel == "telephone") {
                    tel.innerHTML = "";
                }

                if(new_sel == contactPrefere && contactPrefere == "email"){
                    document.getElementById("btnValider").disabled = true;
                }else{
                    document.getElementById("btnValider").disabled = false;
                }
                last_sel = $(radio).data('title');
            }

            function onValidateChange() {
                var saveContact = new Object();
                saveContact.todo = "enregistreContactPrivilegie";
                saveContact.contactPrivilegie = last_sel;
                if (last_sel == "email"){
                    saveContact.numeroTelephone = "";
                }else{
                    saveContact.numeroTelephone = document.getElementById("tel").value;
                }
                console.log(saveContact);
                $.ajax({
                    type: 'POST',
                    url: './ActionServlet',
                    data: saveContact,
                    timeout: 3000,
                    dataType:'json',
                    success: function (data) {
                        console.log(data);
                        if (data.saved){
                            alert("Changements pris en compte");
                            window.location.replace("./filActu.html");
                        }else{
                            $("#erreur").modal();
                        }
                    },
                    error: function (data) {
                        console.log(data);
                        $("#erreur").modal();
                    }
                });
            }
            
            function unsubscribe() {
                var unsubscribe = new Object();
                unsubscribe.todo = "supprimerPersonne";
                var conf = confirm('Etes-vous sûr de vouloir supprimer votre compte ?');
                if(conf){
                    $.ajax({
                        type: 'POST',
                        url: './ActionServlet',
                        data: unsubscribe,
                        timeout: 3000,
                        dataType:'json',
                        success: function (data) {
                            if (data.deleted){
                                window.location.replace("./index.html");
                            }else{
                                $("#erreur").modal();
                            }
                        },
                        error: function (data) {
                            console.log(data);
                            $("#erreur").modal();
                        }
                    });
                }
            }