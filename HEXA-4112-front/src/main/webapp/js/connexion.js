$(document).ready(function () {
                var answers = new Object();
                document.getElementById("valider").addEventListener("click", function () {

                    var send = true;
                    var answers = new Object();
                    answers.todo = "connexion";
                    
                    //Email
                    if (document.getElementById("mail").value != "") {
                        answers.mail = document.getElementById("mail").value;
                        document.getElementById("mail").classList.remove("is-invalid");
                    } else {
                        send = false;
                        document.getElementById("mail").classList.add("is-invalid");
                    }

                    //MDP
                    if (document.getElementById("mdp").value != "") {
                        answers.motDePasse = document.getElementById("mdp").value;
                        document.getElementById("mdp").classList.remove("is-invalid");
                    } else {
                        send = false;
                        document.getElementById("mdp").classList.add("is-invalid");
                    }

                    if (send) {
                        //Validation
                        console.log(answers);
                            $.ajax({
                                type: 'POST',
                                url: './ActionServlet',
                                data: answers,
                                dataType:'json',
                                success: function (data) {
                                    console.log(data);                                
                                    if(data.connected){
                                        window.location.replace(
                                        "./filActu.html");
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
                        
                    };
                });
            });