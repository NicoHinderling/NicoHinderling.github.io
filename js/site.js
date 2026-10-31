// The whole site is one iPod-style screen: a menu and a text page, driven
// by arrow keys, mouse wheel, clicks or taps.
(function () {
  var MENU = {
    title: "Nico Hinderling's site",
    items: [
      { label: "GitHub", href: "http://github.nicohinderling.com" },
      { label: "LinkedIn", href: "http://linkedin.nicohinderling.com" },
      { label: "Resume (2023)", href: "http://resume.nicohinderling.com" },
      { label: "Fun zone", text: '<p>From messing around lately...</p><a class="callout" href="https://casinoteacher.app"><b>casinoteacher.app</b><span>Learn blackjack, roulette, craps, poker and baccarat for free. No real money.</span></a>' }
    ]
  };

  var bar = document.getElementById("bar");
  var title = document.getElementById("title");
  var view = document.getElementById("view");

  var stack = [{ node: MENU, i: 0 }];
  var audio;

  function top() { return stack[stack.length - 1]; }

  function tick() {
    audio = audio || new (window.AudioContext || window.webkitAudioContext)();
    if (audio.state === "suspended") audio.resume();
    var osc = audio.createOscillator();
    var gain = audio.createGain();
    var now = audio.currentTime;
    osc.type = "square";
    osc.frequency.value = 1700;
    gain.gain.value = 0.025;
    osc.connect(gain).connect(audio.destination);
    osc.start(now);
    osc.stop(now + 0.006);
  }

  function draw() {
    var at = top(), node = at.node;
    title.textContent = (stack.length > 1 ? "‹ " : "") + (node.title || node.label);
    bar.disabled = stack.length === 1;
    view.innerHTML = "";
    view.scrollTop = 0;
    if (node.items) {
      node.items.forEach(function (item, i) {
        var row = document.createElement(item.href ? "a" : "button");
        row.className = "row" + (i === at.i ? " on" : "");
        if (item.href) row.href = item.href;
        row.innerHTML = "<span>" + item.label + "</span><span>" + (item.href ? "" : "&rsaquo;") + "</span>";
        row.addEventListener("click", function (e) {
          at.i = i;
          if (!item.href) { e.preventDefault(); select(); }
        });
        view.appendChild(row);
      });
      var on = view.querySelector(".on");
      if (on.offsetTop + on.offsetHeight > view.clientHeight) view.scrollTop = on.offsetTop + on.offsetHeight - view.clientHeight;
    } else if (node.text) {
      view.innerHTML = '<div class="text">' + node.text + "</div>";
    }
  }

  function move(d) {
    var at = top(), node = at.node;
    tick();
    if (node.items) {
      at.i = Math.max(0, Math.min(node.items.length - 1, at.i + d));
      draw();
    } else if (node.text) {
      view.scrollTop += d * view.clientHeight * 0.3;
    }
  }

  function select() {
    var at = top(), item = at.node.items && at.node.items[at.i];
    if (!item) {
      var link = view.querySelector("a");
      if (link) link.click();
      return;
    }
    if (item.href) { view.querySelector(".on").click(); return; }
    stack.push({ node: item, i: 0 });
    draw();
  }

  function back() {
    if (stack.length > 1) { stack.pop(); draw(); }
  }

  document.addEventListener("keydown", function (e) {
    if (e.metaKey || e.ctrlKey || e.altKey) return;
    var onControl = e.target.closest && e.target.closest("a, button");
    if (e.key === "ArrowDown" || e.key === "ArrowRight") { e.preventDefault(); move(1); }
    else if (e.key === "ArrowUp" || e.key === "ArrowLeft") { e.preventDefault(); move(-1); }
    else if (e.key === "Escape" || e.key === "Backspace") { e.preventDefault(); back(); }
    else if (e.key === "Enter" && !onControl) { e.preventDefault(); select(); }
  });

  var scrolled = 0;
  view.addEventListener("wheel", function (e) {
    e.preventDefault();
    scrolled += e.deltaY;
    if (Math.abs(scrolled) > 40) { move(scrolled > 0 ? 1 : -1); scrolled = 0; }
  }, { passive: false });

  bar.addEventListener("click", back);
  draw();
})();
