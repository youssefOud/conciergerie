$(document).ready(function () {
            document.getElementById("valider").addEventListener("click", function () {

            var send = true;
            //Email
            if (document.getElementById("mail").value != "" && document.getElementById("mail").validity.valid) {


            document.getElementById("mail").classList.remove("is-invalid");
            } else {
                    send = false;
            document.getElementById("mail").classList.add("is-invalid");
            }
            
            //MDP
            if (document.getElementById("mdp").value != "") {
            document.getElementById("mdp").classList.remove("is-invalid");
        } else {
                    send = false;
            document.getElementById("mdp").classList.add("is-invalid");
        }
        
        //Nom
            if (document.getElementById("nom").value != "") {
            document.getElementById("nom").classList.remove("is-invalid");
                    } else {
                    send = false;
            document.getElementById("nom").classList.add("is-invalid");
                    }
                
                //Prénom
                if (document.getElementById("prenom").value != "") {
            document.getElementById("prenom").classList.remove("is-invalid");
                    } else {
                    send = false;
            document.getElementById("prenom").classList.add("is-invalid");
                    }
                    
                    var firstSend = new Object();
                    firstSend.mail =  document.getElementById("mail").value;
                    firstSend.todo = "generationCode";
                    if (send) {
                    //Validation
                    $.ajax({
                    type: 'POST',
                            url: './ActionServlet',
                            data: firstSend,
//                            timeout: 3000,
                            dataType:'json',
                            success: function (data) {
                            console.log(data);
                            if (data.emailSent){
                            $("#validationCode").modal();
                            
                            
                    }
                    else{
                                    $("#erreurDejaInscrit").modal();
                    }
                    },
                error: function (data) {
                                    console.log(data);
                                    $("#erreurReseau").modal();
                    }
                    });
                    
                    };
                });
                document.getElementById("verifierCode").addEventListener("click", function () {
                                            var answers = new Object();
                                            answers.todo = "inscription";
                                            answers.mail = document.getElementById("mail").value;
                                            answers.motDePasse = document.getElementById("mdp").value;
                                            answers.nom = document.getElementById("nom").value;
                                            answers.prenom = document.getElementById("prenom").value;
                                            answers.code = document.getElementById("codeIncorrect").value;
                                            $.ajax({
                                                type: 'POST',
                                                url: './ActionServlet',
                                                data: answers,
                                                timeout: 3000,
                                                dataType:'json',
                                                success: function (data) {
                                                    console.log(data);                                
                                                    if(data.registered ){
                                                        window.location.replace(
                                                        "./filActu.html");
                                                    }
                                                    else{
                                                        document.getElementById("codeIncorrect").classList.add("is-invalid");
                                                    }
                                                },
                                                error: function () {
                                                    $("#erreurReseau").modal();
                                                }
                                            });



                            });
                });

