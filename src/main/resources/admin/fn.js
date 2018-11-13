function hideElem(n) {
	var x = document.getElementById(n);
	if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}