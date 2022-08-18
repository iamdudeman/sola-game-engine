var solaCanvas = document.getElementById("sola-canvas");
var solaContext2d = solaCanvas.getContext("2d");
var solaWorker = new Worker("sola_webworker_poc.js");

solaWorker.onmessage = function (event) {
  console.log("main received", event.data);

  var payload = event.data.payload;

  switch (event.data.type) {
    case "initKeyboard": {
      initializeKeyboardForEvent(payload.eventName);
      break;
    }
    case "initMouse": {
      initializeMouseForEvent(payload.eventName);
      break;
    }
    case "render": {
      render(payload.rendererData, payload.width, payload.height, payload.viewportX, payload.viewportY, payload.viewportWidth, payload.viewportHeight);
      break;
    }
  }
}

initCanvas();

////////////// utility stuff past here

function initCanvas() {
  function resizeCanvas() {
    solaWorker.postMessage({
      type: "resize",
      body: {
        width: solaCanvas.width,
        height: solaCanvas.height,
      }
    });
  }

  new ResizeObserver(resizeCanvas).observe(window.solaCanvas);

  function onWindowResize() {
    window.solaCanvas.width = window.innerWidth;
    window.solaCanvas.height = window.innerHeight;
  }

  window.addEventListener("resize", onWindowResize);

  solaCanvas.width = window.innerWidth;
  solaCanvas.height = window.innerHeight;

  solaCanvas.oncontextmenu = function(e) {
    e.preventDefault(); e.stopPropagation();
  };

  solaCanvas.focus();
}

function initializeKeyboardForEvent(eventName) {
  window.keyboardListeners = window.keyboardListeners || {};

  if (window.keyboardListeners[eventName]) {
    window.removeEventListener(eventName, window.keyboardListeners[eventName], false);
  }

  window.keyboardListeners[eventName] = function(event) {
    if (event.target === window.solaCanvas) {
      event.stopPropagation();
      event.preventDefault();

      solaWorker.postMessage({
        type: "keyboard",
        body: {
          eventName: eventName,
          keyCode: event.keyCode,
        },
      });
    }
  };

  window.addEventListener(eventName, window.keyboardListeners[eventName], false);
}

function initializeMouseForEvent(eventName) {
  window.mouseListeners = window.mouseListeners || {};

  if (window.mouseListeners[eventName]) {
    solaCanvas.removeEventListener(eventName, window.mouseListeners[eventName], false);
  }

  window.mouseListeners[eventName] = function(event) {
    if (event.target === window.solaCanvas) {
      var rect = event.target.getBoundingClientRect();
      var x = event.clientX - rect.left;
      var y = event.clientY - rect.top;

      solaWorker.postMessage({
        type: "keyboard",
        body: {
          eventName: eventName,
          which: event.which,
          x: x,
          y: y,
        },
      });
    }
  };

  solaCanvas.addEventListener(eventName, window.mouseListeners[eventName], false);
}

function render(rendererData, width, height, viewportX, viewportY, viewportWidth, viewportHeight) {
  solaContext2d.clearRect(0, 0, solaCanvas.width, solaCanvas.height);

  var imageData = new ImageData(Uint8ClampedArray.from(rendererData), width, height);

  var tempCanvas = document.createElement("canvas");

  tempCanvas.width = width;
  tempCanvas.height = height;
  tempCanvas.getContext("2d").putImageData(imageData, 0, 0);

  window.solaContext2d.drawImage(tempCanvas, viewportX, viewportY, viewportWidth, viewportHeight);
}
