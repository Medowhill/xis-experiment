var id = ''
var key = ''

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
  $('#prev').prop('disabled', curr() === 1 || key === '')
  $('#next').prop('disabled', key === '')
}
function clear() {
  key = ''
  $('#answ').val('')
  $('#link').val('')
  $('#keyw').val('')
  $('#page').html('')
  $('#qnum').html('')
  $('#ques').html('')
  $('#expl').html('')
}

function login() {
  id = $('#uid').val()
  if (id !== "") {
    $('#ldiv').prop('hidden', true)
    $('#qdiv').prop('hidden', false)
    $('#keyw')[0].addEventListener("keyup", e => { if (e.keyCode === 13) search() })
    $('#link')[0].addEventListener("keyup", e => { if (e.keyCode === 13) submit() })
    axios.get('/api/question', {params: {qnum: 0}}).then(show)
  }
}

function search() {
  key = encodeURIComponent($('#keyw').val())
  if (key !== "") {
    $('#page').html(1)
    disable()
    axios.get('/api/search', {params: {key: key, page: 1}}).then(res => {
      $('#main').html(res.data)
      enable()
    })
  }
}

function next() {
  var page = curr() + 1
  $('#page').html(page)
  disable()
  axios.get('/api/search', {params: {key: key, page: page}}).then(res => {
    $('#main').html(res.data)
    enable()
  })
}

function prev() {
  var page = curr() - 1
  $('#page').html(page)
  disable()
  axios.get('/api/search', {params: {key: key, page: page}}).then(res => {
    $('#main').html(res.data)
    enable()
  })
}

function submit() {
  var qnum = currq()
  var ans = encodeURIComponent($('#answ').val())
  var link = encodeURIComponent($('#link').val())
  if (ans !== "" && (ans === "-" || link.startsWith("https://portal.kaist.ac.kr/ennotice/"))) {
    disable()
    clear()
    $('#qnum').html(qnum + 1)
    axios.get('/api/question', {params: {qnum: qnum + 1}}).then(show)
    axios.get('/api/submit', {params: {id: id, qnum: qnum, ans: ans, link: link}})
  } else {
    alert('정답과 링크를 바르게 입력하세요.')
  }
}

function show(res) {
  var fin = res.data.fin
  var q = res.data.question
  var d = res.data.type

  if (fin) {
    $('#qdiv').prop('hidden', true)
    $('#fdiv').prop('hidden', false)
  } else {
    $('#ques').html(q)
    if (d === 'd')
      $('#expl').html('yyyyddmm의 8자리 수로 입력하세요. 예: 19700101')
    else if (d === 't')
      $('#expl').html('hh:mm-hh:mm의 형태로 입력하세요. (24시간제) 예: 12:00-14:00')
    else if (d === 'n')
      $('#expl').html('한글로 사람 이름을 입력하세요. 예: 홍길동')
    else
      $('#expl').html('')
    enable()
  }
}
