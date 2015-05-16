<foo>

  <h3>{ opts.title }</h3>

  <ul>
    <li each={ items }>
       { title }
    </li>
  </ul>

  <form onsubmit={ add }>
    <input name="input" onkeyup={ edit }>
    <button disabled={ !text } click={ add }>Post</button>
  </form>

  this.disabled = true
  this.items = []

  edit(e) {
    this.text = e.target.value
  }

  add(e) {
    if (this.text) {
      opts.store.post(this.text)
      this.text = this.input.value = ''
    }
  }

  toggle(e) {
    var item = e.item
    item.done = !item.done
    return true
  }

</foo>