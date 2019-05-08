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
            }
                            },
                        error: function () {
                                    $("#erreur").modal();
                }
                });
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
                });