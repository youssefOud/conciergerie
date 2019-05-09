$(document).ready(function () {var session = new Object();
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
                document.getElementById("bonjour").innerHTML = "Bonjour, " + data.prenom;
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
                        var answers = new Object();
                        document.getElementById("valider").addEventListener("click", function () {

                var send = true;
                        var answers = new Object();
                        answers.todo = "deposerAnnonce";
                        //Type
                        answers.type = "demande";
                        if ($('#offre').is(':checked')) {
                answers.type = "offre";
                }

//Catégorie
                answers.categorie = document.getElementById('categories').value;
                        //Nom objet
                        if (document.getElementById("objet").value != "") {
                answers.objet = document.getElementById("objet").value;
                        document.getElementById("objet").classList.remove("is-invalid");
                } else {
                send = false;
                        document.getElementById("objet").classList.add("is-invalid");
                }

                //Description
                answers.description = document.getElementById("description").value;
                        //Date
                        if (document.getElementById("date").value != "" && document.getElementById("time").value != "") {
                answers.date = document.getElementById("date").value;
                        answers.time = document.getElementById("time").value;
//                        var current=new Date(Date.now());
                        var wanted = new Date(answers.date.split('/')[2], answers.date.split('/')[1] - 1, answers.date.split('/')[0], answers.time.split(':')[0], answers.time.split(':')[1]);
                        console.log(wanted);
                        if (Date.now() <= wanted.getTime()){
                document.getElementById("date").classList.remove("is-invalid");
                        document.getElementById("time").classList.remove("is-invalid");
                }
                else{
                send = false;
                        document.getElementById("date").classList.add("is-invalid");
                        document.getElementById("time").classList.add("is-invalid");
                }


                } else if (document.getElementById("date").value == ""){
                send = false;
                        document.getElementById("date").classList.add("is-invalid");
                }
                if (document.getElementById("time").value == ""){
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

                //Localisation
                answers.localisation = document.getElementById('localisations').value;
                        //NbPts
                        if (document.getElementById("nbPts").value != "") {
                answers.nbPts = document.getElementById("nbPts").value;
                        document.getElementById("nbPts").classList.remove("is-invalid");
                } else {
                send = false;
                        document.getElementById("nbPts").classList.add("is-invalid");
                }
                //Unité prix
                answers.unitePrix = "heures";
                        if ($('#joursPrix').is(':checked')) {
                answers.unitePrix = "jours";
                } else if ($('#minutesPrix').is(':checked')) {
                answers.unitePrix = "minutes";
                }

                
                        
                //                    console.log("p "+picturesArray);
//                                    answers.pictures = picturesArray;
                console.log(answers);
                        if (send) {
                //Validation
                $("#validation").modal();
                
                };
                });
                }
                },
                error: function () {
                $("#erreur").modal();
                }
        });
        document.getElementById("deposer").addEventListener("click", function () {

var send = true;
        var answers = new Object();
        answers.todo = "deposerAnnonce";
        //Type
        answers.type = "demande";
        if ($('#offre').is(':checked')) {
answers.type = "offre";
}

//Catégorie
answers.categorie = document.getElementById('categories').value;
        //Nom objet
        if (document.getElementById("objet").value != "") {
answers.objet = document.getElementById("objet").value;
        document.getElementById("objet").classList.remove("is-invalid");
} else {
send = false;
        document.getElementById("objet").classList.add("is-invalid");
}

//Description
answers.description = document.getElementById("description").value;
        //Date
        if (document.getElementById("date").value != "" && document.getElementById("time").value != "") {
answers.date = document.getElementById("date").value;
        answers.time = document.getElementById("time").value;
//                        var current=new Date(Date.now());
        var wanted = new Date(answers.date.split('/')[2], answers.date.split('/')[1] - 1, answers.date.split('/')[0], answers.time.split(':')[0], answers.time.split(':')[1]);
        console.log(wanted);
        if (Date.now() <= wanted.getTime()){
document.getElementById("date").classList.remove("is-invalid");
        document.getElementById("time").classList.remove("is-invalid");
}
else{
send = false;
        document.getElementById("date").classList.add("is-invalid");
        document.getElementById("time").classList.add("is-invalid");
}


} else if (document.getElementById("date").value == ""){
send = false;
        document.getElementById("date").classList.add("is-invalid");
}
if (document.getElementById("time").value == ""){
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

//Localisation
answers.localisation = document.getElementById('localisations').value;
        //NbPts
        if (document.getElementById("nbPts").value != "") {
answers.nbPts = document.getElementById("nbPts").value;
        document.getElementById("nbPts").classList.remove("is-invalid");
} else {
send = false;
        document.getElementById("nbPts").classList.add("is-invalid");
}
//Unité prix
answers.unitePrix = "heures";
        if ($('#joursPrix').is(':checked')) {
answers.unitePrix = "jours";
} else if ($('#minutesPrix').is(':checked')) {
answers.unitePrix = "minutes";
}

myArrayFile = $('.uploadFile');
        answers.pictures = "";
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
$.ajax({
type: 'POST',
        url: './ActionServlet',
        data: answers,
        timeout: 3000,
        dataType:'json',
        success: function (data) {
        console.log(data);
                if (data.created){
        window.location.replace(
                "./filActu.html?annonce");
        }
        else{

        $("#erreur").modal();
        }
        },
        error: function () {
        $("#erreur").modal();
        }
});
});
});