const container = document.querySelector('.error-container');
const paragraph = container.querySelector('p');


function hideParagraph() {
    paragraph.classList.add('hidden'); 
    
    paragraph.addEventListener('transitionend', function handleTransitionEnd() {
        container.remove();
        paragraph.removeEventListener('transitionend', handleTransitionEnd);
    });
}

paragraph.addEventListener('click', hideParagraph);
setTimeout(hideParagraph, 5000);
