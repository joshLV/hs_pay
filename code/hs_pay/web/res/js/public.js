var laytip={
        waIt:function(){
              var index = layer.load(2, {
			         shade: [0.6,'#fff']
		        });
        },
        cloSe:function(){
        	  layer.closeAll('loading');
        },
        bankUrl:function(){
        },

}

  if(window.hasOwnProperty("ontouchstart")) {
          var _event = 'touchend';
    }else {
          var _event = 'click';
  }