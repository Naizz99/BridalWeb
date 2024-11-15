/*! bootstrap-off-canvas-android-style - v1.0.4
* Copyright (c) 2015 Steffen Ermel; Licensed MIT *
*/
jQuery(document).ready(function($) {

    $('[data-toggle="offcanvas"]').click(function () {
    if ($('.navbar-collapse').hasClass('in')) {
        // Close the navbar, slide it out to the left
        $('.navbar-collapse').removeClass('in').addClass('out');
        $('.overlay').removeClass('active');
        $('body').removeClass('active');
    } else {
        // Open the navbar, slide it in from the left
        $('.navbar-collapse').removeClass('out').addClass('in');
        $('.overlay').addClass('active');
        $('body').addClass('active');
    }
});


    function whichTransitionEvent() {
        var el = document.createElement('event'),
            transitionEvents = {
                'WebkitTransition' : 'webkitTransitionEnd',// Saf 6, Android Browser
                'MozTransition'    : 'transitionend',      // only for FF < 15
                'transition'       : 'transitionend'       // IE10, Opera, Chrome, FF 15+, Saf 7+
            };
        for(var t in transitionEvents){
            if( el.style[t] !== undefined ){
                return transitionEvents[t];
            }
        }
    }
    var transitionEvent = whichTransitionEvent();

    $('.overlay').click(function () {
        $('#navbar').removeClass('in');
        $('.overlay').removeClass('active');
        $('body').removeClass('active');
        $('.transition').one(transitionEvent,

        function(e) {
             $('.navbar-collapse').removeClass('transition');
        });
    });

    $('.navbar .nav a:not(".dropdown-toggle")').click(function () {
        $('#navbar').removeClass('in');
        $('.overlay').removeClass('active');
        $('body').removeClass('active');
        $('.transition').one(transitionEvent,
        function(e) {
             $('.navbar-collapse').removeClass('transition');
        });
    });

    // $(".overlay").swiperight(function() {
    //     $('.overlay').addClass('active');
    //     $('body').addClass('active');
    //     $('#navbar').addClass('in');
    //     $('.navbar-collapse').addClass('transition');
    // });

    // $(".overlay").swipeleft(function() {
    //     $('.overlay').removeClass('active');
    //     $('body').removeClass('active');
    //     $('#navbar').removeClass('in');
    //     $('.transition').one(transitionEvent,
    //         function(e) {
    //              $('.navbar-collapse').removeClass('transition');
    //         });
    // });

});
