var id = ""
var key = ""

function curr() { return Number($('#page').html()) }
function currq() { return Number($('#qnum').html()) }

function disable() {
  $('#subm').prop('disabled', true)
  $('#sear').prop('disabled', true)
  $('#prev').prop('disabled', true)
  $('#next').prop('disabled', true)
  $('#main').html('')
}
function enable() {
  $('#subm').prop('disabled', false)
  $('#sear').prop('disabled', false)
  $('#prev').prop('disabled', curr() === 1)
  $('#next').prop('disabled', false)
}

function search() {
  key = $('#keyw').val()
  if (key !== "") {
    $('#page').html(1)
    disable()
    axios.get('/api/search/' + key + '/1').then(res => {
      $('#main').html(res.data)
      enable()
    })
  }
}

function next() {
  var page = curr() + 1
  $('#page').html(page)
  disable()
  axios.get('/api/search/' + key + '/' + page).then(res => {
    $('#main').html(res.data)
    enable()
  })
}

function prev() {
  var page = curr() - 1
  $('#page').html(page)
  disable()
  axios.get('/api/search/' + key + '/' + page).then(res => {
    $('#main').html(res.data)
    enable()
  })
}

function submit() {
  var qnum = currq()
  var ans = encodeURI($('#answ').val())
  var link = endoeURI($('#link').val())
  if (ans !== "" && (ans === "." || link !== "")) {
    $('#qnum').html(qnum + 1)
    disable()
    axios.get('/api/submit/' + qnum + '/' + ans ).then(res => {
      enable()
    })
  }
}
