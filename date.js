// simple date formatting utility
export function formatDate(value, fmt = 'yyyy-MM-dd HH:mm') {
  if (value === null || value === undefined || value === '') return ''
  const d = new Date(value)
  if (Number.isNaN(d.getTime())) return String(value)

  const pad = (n) => String(n).padStart(2, '0')
  const replacements = {
    yyyy: d.getFullYear(),
    MM: pad(d.getMonth() + 1),
    dd: pad(d.getDate()),
    HH: pad(d.getHours()),
    mm: pad(d.getMinutes()),
    ss: pad(d.getSeconds()),
  }

  let out = fmt
  Object.keys(replacements).forEach((k) => {
    out = out.replace(k, replacements[k])
  })
  return out
}

// legacy alias
export const dateF = formatDate
